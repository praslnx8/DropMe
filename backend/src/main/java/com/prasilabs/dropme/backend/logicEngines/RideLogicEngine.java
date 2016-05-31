package com.prasilabs.dropme.backend.logicEngines;

import com.google.appengine.api.datastore.Query;
import com.googlecode.objectify.Key;
import com.prasilabs.dropme.backend.core.CoreLogicEngine;
import com.prasilabs.dropme.backend.datastore.Ride;
import com.prasilabs.dropme.backend.db.OfyService;
import com.prasilabs.dropme.backend.io.ApiResponse;
import com.prasilabs.dropme.backend.io.VRide;

import java.util.Date;

/**
 * Created by prasi on 31/5/16.
 */
public class RideLogicEngine extends CoreLogicEngine
{
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

    public ApiResponse createRide(VRide vRide)
    {
        ApiResponse apiResponse = new ApiResponse();

        Ride ride = convertToRide(vRide);

        if(validateRide(ride))
        {
            Query.Filter dateFilter = new Query.FilterPredicate(Ride.EXPIRY_DATE_STR, Query.FilterOperator.GREATER_THAN, new Date(System.currentTimeMillis()));
            Ride existingRide = OfyService.ofy().load().type(Ride.class).filter(dateFilter).filter(Ride.USER_ID_STR, ride.getUserId()).filter(Ride.DEVICE_ID_STR, ride.getDeviceId()).first().now();

            if(existingRide == null)
            {
                Key<Ride> rideKey = OfyService.ofy().save().entity(ride).now();
                apiResponse.setStatus(true);
                apiResponse.setId(rideKey.getId());
            }
            else
            {
                apiResponse.setMessage("Already on a ride. Cancel the ride first");
            }
        }
        else
        {
            apiResponse.setMessage("not a valid ride");
        }

        return apiResponse;
    }


    private static Ride convertToRide(VRide vRide)
    {
        Ride ride = new Ride();

        if(vRide.getId() != 0)
        {
            ride.setId(vRide.getId());
        }
        ride.setSourceLoc(vRide.getSourceLoc());
        ride.setDestLoc(vRide.getDestLoc());
        ride.setCurrentLoc(vRide.getCurrentLoc());
        ride.setDeviceId(vRide.getDeviceId());
        ride.setUserId(vRide.getUserId());
        ride.setEndDate(vRide.getEndDate());
        ride.setFarePerKm(vRide.getFarePerKm());
        ride.setVehicleId(vRide.getVehicleId());

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
            }
            else if(ride.getSourceLoc() == null)
            {
                isValid = false;
            }
            else if(ride.getDestLoc() == null)
            {
                isValid = false;
            }
            else if(ride.getDeviceId() == 0L)
            {
                isValid = false;
            }
            else if(ride.getUserId() == 0L)
            {
                isValid = false;
            }
        }

        return isValid;
    }
}
