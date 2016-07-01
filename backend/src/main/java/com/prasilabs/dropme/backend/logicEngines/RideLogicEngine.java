package com.prasilabs.dropme.backend.logicEngines;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.apphosting.api.DatastorePb;
import com.googlecode.objectify.Key;
import com.prasilabs.dropme.backend.annotions.Cron;
import com.prasilabs.dropme.backend.core.CoreLogicEngine;
import com.prasilabs.dropme.backend.datastore.DropMeUser;
import com.prasilabs.dropme.backend.datastore.Ride;
import com.prasilabs.dropme.backend.datastore.Vehicle;
import com.prasilabs.dropme.backend.db.OfyService;
import com.prasilabs.dropme.backend.debug.ConsoleLog;
import com.prasilabs.dropme.backend.io.ApiResponse;
import com.prasilabs.dropme.backend.io.MyRideInfo;
import com.prasilabs.dropme.backend.io.RideDetail;
import com.prasilabs.dropme.backend.io.RideInput;
import com.prasilabs.dropme.backend.io.VVehicle;
import com.prasilabs.dropme.backend.services.geofire.GeoFireManager;
import com.prasilabs.dropme.backend.services.places.PlaceUtil;
import com.prasilabs.dropme.backend.services.pushquees.PushQueueController;
import com.prasilabs.dropme.backend.utils.DistanceCalculator;
import com.prasilabs.dropme.backend.utils.RideUtil;
import com.prasilabs.dropme.backend.utils.SortWrapper;
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

    public RideLogicEngine() {
    }

    public static RideLogicEngine getInstance()
    {
        if(instance == null)
        {
            instance = new RideLogicEngine();
        }
        return instance;
    }

    private static RideInput convertToRideInout(Ride ride) {
        RideInput rideInput = null;

        if (ride != null) {
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
            rideInput.setMessage(ride.getMessage());
        }

        return rideInput;
    }

    private static Ride convertToRide(RideInput rideInput) {
        Ride ride = new Ride();

        if (rideInput.getId() != 0) {
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
        ride.setSourceLocName(PlaceUtil.getLocalityName(rideInput.getSourceLoc()));
        ride.setMessage(rideInput.getMessage());

        return ride;
    }

    private static boolean validateRide(Ride ride) {
        boolean isValid = false;

        if (ride != null) {
            isValid = true;

            if (ride.getCurrentLoc() == null) {
                isValid = false;
                ConsoleLog.w(TAG, "current loc is null");
            } else if (ride.getSourceLoc() == null) {
                isValid = false;
                ConsoleLog.w(TAG, "source loc is null");
            } else if (ride.getDestLoc() == null) {
                isValid = false;
                ConsoleLog.w(TAG, "dest loc is null");
            } else if (DataUtil.isEmpty(ride.getDeviceId())) {
                isValid = false;
                ConsoleLog.w(TAG, "device id is null");
            } else if (ride.getUserId() == 0L) {
                isValid = false;
                ConsoleLog.w(TAG, "user id null");
            } else if (DataUtil.isEmpty(ride.getDestLocName())) {
                isValid = false;
                ConsoleLog.w(TAG, "dest loc name is null");
            } else if (ride.getExpiryDate() == null || ride.getExpiryDate().before(new Date(System.currentTimeMillis()))) {
                isValid = false;
                ConsoleLog.w(TAG, "expiry date is not valid");
            }
        }

        return isValid;
    }

    private static RideDetail convertRideToRideDetail(Ride ride) {
        RideDetail rideDetail = null;
        boolean isValid = true;

        Vehicle vehicle = null;
        if (isValid) {
            vehicle = OfyService.ofy().load().type(Vehicle.class).id(ride.getVehicleId()).now();
            if (vehicle == null) {
                isValid = false;
            }
        }
        DropMeUser dropMeUser = null;
        if (isValid) {
            dropMeUser = OfyService.ofy().load().type(DropMeUser.class).id(ride.getUserId()).now();
            if (dropMeUser == null) {
                isValid = false;
            }
        }

        if (isValid) {
            rideDetail = new RideDetail();
            rideDetail.setRideId(ride.getId());
            rideDetail.setStartDate(ride.getStartDate());
            rideDetail.setSourceLatLng(ride.getSourceLoc());
            rideDetail.setDestLatLng(ride.getDestLoc());
            rideDetail.setDestLoc(ride.getDestLocName());
            rideDetail.setCurrentLatLng(ride.getCurrentLoc());
            rideDetail.setOwnerId(ride.getUserId());
            rideDetail.setVehicleNumber(vehicle.getvNumber());
            rideDetail.setVehicleType(vehicle.getType());
            rideDetail.setGender(dropMeUser.getGender());
            rideDetail.setOwnerName(dropMeUser.getName());
            rideDetail.setOwnerPhone(dropMeUser.getMobile());
            rideDetail.setOwnerPicture(dropMeUser.getPicture());
            rideDetail.setMessage(ride.getMessage());
        }

        return rideDetail;
    }

    public ApiResponse updateRide(User user, RideInput rideInput)
    {
        ApiResponse apiResponse = new ApiResponse();

        DropMeUser dropMeUser = DropMeUserLogicEngine.getInstance().getDropMeUser(user.getEmail());

        Ride ride = convertToRide(rideInput);

        Ride rideFromDb = OfyService.ofy().load().type(Ride.class).id(ride.getId()).now();

        if(rideFromDb != null)
        {
            if(rideFromDb.getUserId() == dropMeUser.getId())
            {
                rideFromDb.setCurrentLoc(rideInput.getCurrentLoc());

                Key<Ride> rideKey = OfyService.ofy().save().entity(rideFromDb).now();

                apiResponse.setStatus(true);
                apiResponse.setId(rideKey.getId());
            }
            else
            {
                ConsoleLog.s(TAG, "secirity issue. User try to edit other data");
                apiResponse.setMessage("you are not the right user to update the ride");
            }
        }
        else
        {
            apiResponse.setMessage("ride not found not found");
        }

        return apiResponse;
    }

    public RideInput createRide(User user, RideInput rideInput)
    {
        DropMeUser dropMeUser = DropMeUserLogicEngine.getInstance().getDropMeUser(user.getEmail());
        if (dropMeUser != null && dropMeUser.getMobile() != null && dropMeUser.isMobileVerified())
        {
            rideInput.setUserId(dropMeUser.getId());

            Ride ride = convertToRide(rideInput);
            RideUtil.calculateAndSetExpiry(ride);

            if (validateRide(ride))
            {
                Ride currentRide = getExistingActiveRide(rideInput.getUserId(), rideInput.getDeviceId());

                if (currentRide == null)
                {
                    ride.setCurrentLoc(rideInput.getSourceLoc());
                    ride.setCreated(new Date(System.currentTimeMillis()));
                    ride.setModified(new Date(System.currentTimeMillis()));
                    Key<Ride> rideKey = OfyService.ofy().save().entity(ride).now();
                    ride.setId(rideKey.getId());

                    PushQueueController.sendRideAlert(ride);
                }
                else
                {
                    ConsoleLog.w(TAG, "ride already exist");
                    ride = null;
                }
            } else {
                ConsoleLog.w(TAG, "validation failed");
                ride = null;
            }

            rideInput = convertToRideInout(ride);
        }
        else
        {
            ConsoleLog.w(TAG, "user is null or mobile not confirmed");
        }


        return rideInput;
    }

    public RideInput getCurrentRide(User user, String deviceID)
    {
        DropMeUser dropMeUser = DropMeUserLogicEngine.getInstance().getDropMeUser(user.getEmail());

        Ride ride = getExistingActiveRide(dropMeUser.getId(), deviceID);

        RideInput rideInput = convertToRideInout(ride);

        return rideInput;
    }

    public ApiResponse cancelRide(User user, String deviceId)
    {
        ApiResponse apiResponse = new ApiResponse();

        try
        {
            DropMeUser dropMeUser = DropMeUserLogicEngine.getInstance().getDropMeUser(user.getEmail());

            if(dropMeUser != null)
            {
                Query.Filter dateFilter = new Query.FilterPredicate(Ride.EXPIRY_DATE_STR, Query.FilterOperator.GREATER_THAN, new Date(System.currentTimeMillis()));
                List<Ride> rideList = OfyService.ofy().load().type(Ride.class).filter(dateFilter).filter(Ride.USER_ID_STR, dropMeUser.getId()).filter(Ride.DEVICE_ID_STR, deviceId).filter(Ride.IS_CLOSED_STR, false).list();

                if (rideList != null)
                {
                    for (Ride ride : rideList)
                    {
                        ride.setClosed(true);
                        ride.setClosedDate(new Date(System.currentTimeMillis()));
                    }
                    Map<Key<Ride>, Ride> rideMapKey = OfyService.ofy().save().entities(rideList).now();
                }
                apiResponse.setStatus(true);
            }
            else
            {
                ConsoleLog.w(TAG, "user is null");
            }
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }

        return apiResponse;
    }

    @Cron
    public ApiResponse deleteInactiveGeoRideKeys()
    {
        ApiResponse apiResponse = new ApiResponse();

        try {
            Query.Filter dateFilter = new Query.FilterPredicate(Ride.EXPIRY_DATE_STR, Query.FilterOperator.LESS_THAN, new Date(System.currentTimeMillis()));
            List<Ride> rideList = OfyService.ofy().load().type(Ride.class).filter(dateFilter).filter(Ride.IS_GEO_REMOVED_STR, false).list();

            for (Ride ride : rideList)
            {
                String rideGeoKEy = GeoFireKeyGenerator.generateRideKey(ride.getId());

                GeoFireManager.removeGeoPoint(rideGeoKEy);

                ride.setGeoRemoved(true);
            }

            OfyService.ofy().save().entities(rideList).now();

            ConsoleLog.l(TAG, "removed " + rideList.size() + " geopoints from the geoFire server");

            apiResponse.setStatus(true);
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }

        return apiResponse;
    }

    public RideDetail getRideDetail(long id)
    {
        List<Long> ids = new ArrayList<>();
        ids.add(id);

        List<RideDetail> rideDetails = getRideDetailList(ids);

        if(rideDetails != null && rideDetails.size() > 0)
        {
            return rideDetails.get(0);
        }

        return null;
    }

    public List<RideDetail> getRideDetailList(List<Long> ids)
    {
        return getRideDetailList(ids,null);
    }

    public List<RideDetail> getRideDetailList(List<Long> ids, GeoPt dest)
    {
        ConsoleLog.i(TAG, "input ids list is : " + ids.size());

        if(dest != null)
        {
            if((dest.getLatitude() > 0 || dest.getLatitude() < 0) && (dest.getLongitude() >0 || dest.getLongitude() < 0))
            {
                //do nothing
            }
            else
            {
                dest = null;
            }
            ConsoleLog.i(TAG, "dest is not null and invalid. Setting to null");
        }

        List<RideDetail> rideDetails = new ArrayList<>();

        Map<Long, Ride> rideMap = OfyService.ofy().load().type(Ride.class).ids(ids);

        for(Map.Entry<Long, Ride> entry : rideMap.entrySet())
        {
            boolean isValid = true;

            Ride ride = entry.getValue();

            if(ride.getExpiryDate().after(new Date(System.currentTimeMillis())) && !ride.isClosed())
            {
                RideDetail rideDetail = convertRideToRideDetail(ride);

                if(dest != null)
                {
                    GeoPt geoPt = rideDetail.getDestLatLng();
                    if(!DistanceCalculator.isFit(geoPt, dest, 0.5))
                    {
                        isValid = false;
                    }
                    else
                    {
                        ConsoleLog.i(TAG, "distance is more");
                    }
                }

                if (isValid)
                {
                    rideDetails.add(rideDetail);
                }
                else
                {
                    ConsoleLog.i(TAG, "invalid ride");
                }
            }
            else
            {
                ConsoleLog.l(TAG, "removing expired or closed");
                String key = GeoFireKeyGenerator.generateRideKey(ride.getId());
                PushQueueController.callRemoveGeoPtPush(key); //TODO not working. Error do in backendinstances Wait for geo fire to fix that
            }
        }


        return rideDetails;
    }

    public List<MyRideInfo> getRideDetailsOfUser(User user, int skip, int resultSize)
    {
        DropMeUser dropMeUser = DropMeUserLogicEngine.getInstance().getDropMeUser(user.getEmail());

        SortWrapper sortWrapper = new SortWrapper(Ride.CREATED_STR, DatastorePb.Query.Order.Direction.DESCENDING);
        List<Ride> rideList = OfyService.ofy().load().type(Ride.class).filter(Ride.USER_ID_STR, dropMeUser.getId()).order(sortWrapper.getSorting()).limit(resultSize).offset(resultSize * skip).list();

        List<MyRideInfo> myRideInfoList = new ArrayList<>();
        for(Ride ride : rideList)
        {
            MyRideInfo myRideInfo = new MyRideInfo();

            myRideInfo.setId(ride.getId());
            myRideInfo.setSourceLoc(ride.getSourceLoc());
            myRideInfo.setDestLoc(ride.getDestLoc());
            myRideInfo.setDestLocName(ride.getDestLocName());
            myRideInfo.setSourceLocName(ride.getSourceLocName());
            myRideInfo.setFarePerKm(ride.getFarePerKm());
            VVehicle vehicle = VehicleLogicEngine.getInstance().getVVehicleById(ride.getVehicleId());
            myRideInfo.setVehicle(vehicle);
            if(ride.getStartDate() != null)
            {
                myRideInfo.setDate(ride.getStartDate());
            }
            else
            {
                myRideInfo.setDate(ride.getCreated());
            }

            if(ride.getExpiryDate().getTime() > System.currentTimeMillis() && !ride.isClosed())
            {
                myRideInfo.setCurrent(true);
            }
            else
            {
                myRideInfo.setCurrent(false);
            }

            myRideInfo.setMessage(ride.getMessage());

            myRideInfoList.add(myRideInfo);
        }

        return myRideInfoList;
    }

    private Ride getExistingActiveRide(long userId, String deviceId)
    {
        Query.Filter dateFilter = new Query.FilterPredicate(Ride.EXPIRY_DATE_STR, Query.FilterOperator.GREATER_THAN, new Date(System.currentTimeMillis()));
        Ride ride = OfyService.ofy().load().type(Ride.class).filter(dateFilter).filter(Ride.USER_ID_STR, userId).filter(Ride.DEVICE_ID_STR, deviceId).filter(Ride.IS_CLOSED_STR, false).first().now();
        return ride;
    }
}
