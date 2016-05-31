package com.prasilabs.dropme.backend.logicEngines;

import com.google.appengine.api.datastore.Query;
import com.googlecode.objectify.Key;
import com.prasilabs.dropme.backend.core.CoreLogicEngine;
import com.prasilabs.dropme.backend.datastore.DropMeUser;
import com.prasilabs.dropme.backend.datastore.Ride;
import com.prasilabs.dropme.backend.datastore.Vehicle;
import com.prasilabs.dropme.backend.db.OfyService;
import com.prasilabs.dropme.backend.debug.ConsoleLog;
import com.prasilabs.dropme.backend.io.ApiResponse;
import com.prasilabs.dropme.backend.io.RideDetail;
import com.prasilabs.dropme.backend.io.RideInput;
import com.prasilabs.dropme.backend.services.geofire.GeoFireManager;
import com.prasilabs.dropme.backend.utils.RideUtil;
import com.prasilabs.util.DataUtil;
import com.prasilabs.util.GeoFireKeyGenerator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by prasi on 31/5/16.
 */
public class RideLogicEngine extends CoreLogicEngine
{
    private static final String TAG = RideLogicEngine.class.getSimpleName();
    private static RideLogicEngine instance;

    public static RideLogicEngine getInstance()
    {
        if(instance == null)
        {
            instance = new RideLogicEngine();
        }
        return instance;
    }

    public RideLogicEngine(){}

    public RideInput createRide(RideInput rideInput)
    {
        Ride ride = convertToRide(rideInput);
        RideUtil.calculateAndSetExpiry(ride);

        if(validateRide(ride))
        {
            Query.Filter dateFilter = new Query.FilterPredicate(Ride.EXPIRY_DATE_STR, Query.FilterOperator.GREATER_THAN, new Date(System.currentTimeMillis()));
            Ride existingRide = OfyService.ofy().load().type(Ride.class).filter(dateFilter).filter(Ride.USER_ID_STR, ride.getUserId()).filter(Ride.DEVICE_ID_STR, ride.getDeviceId()).filter(Ride.IS_CLOSED_STR, false).first().now();

            if(existingRide == null)
            {
                ride.setCreated(new Date(System.currentTimeMillis()));
                ride.setModified(new Date(System.currentTimeMillis()));
                Key<Ride> rideKey = OfyService.ofy().save().entity(ride).now();
                ride.setVehicleId(rideKey.getId());
            }
            else
            {
                ConsoleLog.w(TAG, "ride already exist");
                ride = null;
            }
        }
        else
        {
            ConsoleLog.w(TAG, "validation failed");
            ride = null;
        }

        rideInput = convertToRideInout(ride);

        return rideInput;
    }

    public RideInput getCurrentRide(DropMeUser dropMeUser, String deviceID)
    {
        Query.Filter dateFilter = new Query.FilterPredicate(Ride.EXPIRY_DATE_STR, Query.FilterOperator.GREATER_THAN, new Date(System.currentTimeMillis()));
        Ride currentRide = OfyService.ofy().load().type(Ride.class).filter(dateFilter).filter(Ride.USER_ID_STR, dropMeUser.getId()).filter(Ride.DEVICE_ID_STR, deviceID).filter(Ride.IS_CLOSED_STR, false).first().now();

        RideInput rideInput = convertToRideInout(currentRide);

        return rideInput;
    }

    public ApiResponse cancelRide(DropMeUser dropMeUser, long rideId)
    {
        ApiResponse apiResponse = new ApiResponse();

        Ride ride = OfyService.ofy().load().type(Ride.class).id(rideId).now();

        if(ride != null && ride.getUserId() == dropMeUser.getId())
        {
            ride.setClosed(true);
            ride.setClosedDate(new Date(System.currentTimeMillis()));
            OfyService.ofy().save().entity(ride).now();
        }

        return apiResponse;
    }

    public List<RideDetail> getRideDetailList(List<Long> ids)
    {
        List<RideDetail> rideDetails = new ArrayList<>();

        Map<Long, Ride> rideMap = OfyService.ofy().load().type(Ride.class).ids(ids);

        for(Map.Entry<Long, Ride> entry : rideMap.entrySet())
        {
            boolean isValid = true;

            Ride ride = entry.getValue();

            if(ride.getExpiryDate().after(new Date(System.currentTimeMillis())) && !ride.isClosed())
            {
                RideDetail rideDetail = new RideDetail();
                rideDetail.setRideId(ride.getId());
                rideDetail.setStartDate(ride.getStartDate());
                rideDetail.setDestLatLng(ride.getDestLoc());
                rideDetail.setDestLoc(ride.getDestLocName());
                Vehicle vehicle = OfyService.ofy().load().type(Vehicle.class).id(ride.getVehicleId()).now();
                if (vehicle != null) {
                    rideDetail.setVehicleNumber(vehicle.getvNumber());
                } else {
                    isValid = false;
                }
                DropMeUser dropMeUser = OfyService.ofy().load().type(DropMeUser.class).id(ride.getUserId()).now();
                if (dropMeUser != null) {
                    rideDetail.setGender(dropMeUser.getGender());
                    rideDetail.setOwnerName(dropMeUser.getName());
                    rideDetail.setOwnerPhone(dropMeUser.getMobile());
                    rideDetail.setOwnerPicture(dropMeUser.getPicture());
                } else {
                    isValid = false;
                }

                if (isValid) {
                    rideDetails.add(rideDetail);
                }
            }
            else
            {
                ConsoleLog.i(TAG, "removing expired or closed");
                String key = GeoFireKeyGenerator.generateRideKey(ride.getId());
                GeoFireManager.getGeoFire().removeLocation(key);
            }
        }


        return rideDetails;
    }

    private static RideInput convertToRideInout(Ride ride) {
        RideInput rideInput = null;

        if (ride != null)
        {
            rideInput = new RideInput();

            rideInput.setId(ride.getId());
            rideInput.setSourceLoc(ride.getSourceLoc());
            rideInput.setDestLoc(ride.getDestLoc());
            rideInput.setCurrentLoc(ride.getCurrentLoc());
            rideInput.setDeviceId(ride.getDeviceId());
            rideInput.setUserId(ride.getUserId());
            rideInput.setFarePerKm(ride.getFarePerKm());
            rideInput.setVehicleId(ride.getVehicleId());
            rideInput.setExpiryDate(ride.getExpiryDate());
            rideInput.setDestLocName(ride.getDestLocName());
            rideInput.setClosed(ride.isClosed());
            rideInput.setClosedDate(ride.getClosedDate());
            rideInput.setStartDate(ride.getStartDate());
        }

        return rideInput;
    }

    private static Ride convertToRide(RideInput rideInput)
    {
        Ride ride = new Ride();

        if(rideInput.getId() != 0)
        {
            ride.setId(rideInput.getId());
        }
        ride.setSourceLoc(rideInput.getSourceLoc());
        ride.setDestLoc(rideInput.getDestLoc());
        ride.setCurrentLoc(rideInput.getCurrentLoc());
        ride.setDeviceId(rideInput.getDeviceId());
        ride.setUserId(rideInput.getUserId());
        ride.setClosedDate(rideInput.getClosedDate());
        ride.setFarePerKm(rideInput.getFarePerKm());
        ride.setVehicleId(rideInput.getVehicleId());
        ride.setDestLoc(rideInput.getDestLoc());
        ride.setDestLocName(rideInput.getDestLocName());

        return ride;
    }

    private static boolean validateRide(Ride ride)
    {
        boolean isValid = false;

        if(ride != null)
        {
            isValid = true;

            if(ride.getCurrentLoc() == null)
            {
                isValid = false;
                ConsoleLog.w(TAG, "current loc is null");
            }
            else if(ride.getSourceLoc() == null)
            {
                isValid = false;
                ConsoleLog.w(TAG, "source loc is null");
            }
            else if(ride.getDestLoc() == null)
            {
                isValid = false;
                ConsoleLog.w(TAG, "dest loc is null");
            }
            else if(DataUtil.isEmpty(ride.getDeviceId()))
            {
                isValid = false;
                ConsoleLog.w(TAG, "device id is null");
            }
            else if(ride.getUserId() == 0L)
            {
                isValid = false;
                ConsoleLog.w(TAG, "user id null");
            }
            else if(DataUtil.isEmpty(ride.getDestLocName()))
            {
                isValid = false;
                ConsoleLog.w(TAG, "dest loc name is null");
            }
            else if(ride.getExpiryDate() == null || ride.getExpiryDate().before(new Date(System.currentTimeMillis())))
            {
                isValid = false;
                ConsoleLog.w(TAG, "expiry date is not valid");
            }
        }

        return isValid;
    }
}
