package com.prasilabs.dropme.backend.logicEngines;

import com.google.api.server.spi.auth.common.User;
import com.google.appengine.api.datastore.Query;
import com.googlecode.objectify.Key;
import com.prasilabs.dropme.backend.datastore.DropMeUser;
import com.prasilabs.dropme.backend.datastore.Ride;
import com.prasilabs.dropme.backend.datastore.RideAlert;
import com.prasilabs.dropme.backend.datastore.Vehicle;
import com.prasilabs.dropme.backend.db.OfyService;
import com.prasilabs.dropme.backend.io.ApiResponse;
import com.prasilabs.dropme.backend.io.RideAlertIo;
import com.prasilabs.dropme.backend.utils.GeoFilter;
import com.prasilabs.util.DataUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by prasi on 8/6/16.
 */
public class RideAlertLogicEngine
{
    private static RideAlertLogicEngine instance;

    private RideAlertLogicEngine(){}

    public static RideAlertLogicEngine getInstance()
    {
        if(instance == null)
        {
            instance = new RideAlertLogicEngine();
        }

        return instance;
    }

    public ApiResponse createRideAlert(User user, RideAlertIo rideAlertIo)
    {
        ApiResponse apiResponse = new ApiResponse();

        RideAlert rideAlert = convertRideAlert(rideAlertIo);
        if(rideAlert != null)
        {
            DropMeUser dropMeUser = DropMeUserLogicEngine.getInstance().getDropMeUser(user.getEmail());
            rideAlert.setId(null);
            rideAlert.setUserId(dropMeUser.getId());
            rideAlert.setCreated(new Date(System.currentTimeMillis()));
            rideAlert.setModified(new Date(System.currentTimeMillis()));

            Key<RideAlert> rideAlertKey = OfyService.ofy().save().entity(rideAlert).now();

            apiResponse.setStatus(true);
            apiResponse.setId(rideAlertKey.getId());
        }

        return apiResponse;
    }

    public ApiResponse deleteRideAlert(User user, long id)
    {
        ApiResponse apiResponse = new ApiResponse();

        RideAlert rideAlert = OfyService.ofy().load().type(RideAlert.class).id(id).now();

        if(rideAlert != null)
        {
            DropMeUser dropMeUser = DropMeUserLogicEngine.getInstance().getDropMeUser(user.getEmail());

            if(rideAlert.getUserId() == dropMeUser.getId())
            {
                rideAlert.setDeleted(true);
                Key<RideAlert> rideAlertKey = OfyService.ofy().save().entity(rideAlert).now();
                apiResponse.setStatus(true);
                apiResponse.setId(rideAlertKey.getId());
            }
        }

        return apiResponse;
    }

    public List<RideAlert> getRidesBasedOnRide(Ride ride)
    {
        List<RideAlert> rideAlertList = new ArrayList<>();

        Query.Filter sourceFilter = new GeoFilter(RideAlert.SOURCE_PT).getFilter(ride.getCurrentLoc(),500);
        Query.Filter destFilter = new GeoFilter(RideAlert.DEST_PT).getFilter(ride.getDestLoc(),500);

        List<RideAlert> dataList = OfyService.ofy().load().type(RideAlert.class).filter(sourceFilter).filter(destFilter).filter(RideAlert.IS_DELETED_STR, false).list();

        filterForTiming(ride, dataList);

        Iterator<RideAlert> rideAlertIterator = dataList.iterator();

        while (rideAlertIterator.hasNext())
        {
            RideAlert rideAlert = rideAlertIterator.next();

            Vehicle vehicle = VehicleLogicEngine.getInstance().getVehicleById(ride.getVehicleId());
            DropMeUser dropMeUser = DropMeUserLogicEngine.getInstance().getDropMeUserById(ride.getUserId());

            if(!(rideAlert.getVehicleType() != null && rideAlert.getVehicleType().equals(vehicle.getType())))
            {
                rideAlertIterator.remove();
            }
            else if(rideAlert.getG //TODOdropMeUser.getGender().equals())
            {


            }

        }


        return rideAlertList;
    }


    public RideAlert convertRideAlert(RideAlertIo rideAlertIo)
    {
        RideAlert rideAlert = null;

        if(rideAlertIo != null)
        {
            boolean isValid = true;
            if(DataUtil.isEmpty(rideAlertIo.getId()))
            {
                isValid = false;
            }
            else if(rideAlertIo.getSource() == null || rideAlertIo.getDest() == null || DataUtil.isEmpty(rideAlertIo.getSourceName()) || DataUtil.isEmpty(rideAlertIo.getDestName()))
            {
                isValid = false;
            }

            if(isValid)
            {
                rideAlert = new RideAlert();

                if(!DataUtil.isEmpty(rideAlertIo.getId()))
                {
                    rideAlert.setId(rideAlertIo.getId());
                }
                rideAlert.setSourcePt(rideAlertIo.getSource());
                rideAlert.setSourceName(rideAlertIo.getSourceName());
                rideAlert.setDestPt(rideAlertIo.getDest());
                rideAlert.setDestName(rideAlertIo.getDestName());
                rideAlert.setGender(rideAlertIo.getGender());
                rideAlert.setVehicleType(rideAlertIo.getVehicleType());
                rideAlert.setStartTime(rideAlertIo.getStartTime());
                rideAlert.setEndTime(rideAlertIo.getEndTime());
            }
        }

        return rideAlert;
    }

    public RideAlertIo concertRideAlert(RideAlert rideAlert)
    {
        RideAlertIo rideAlertIo = null;
        if(rideAlert != null)
        {
            rideAlertIo = new RideAlertIo();
            rideAlertIo.setId(rideAlert.getId());
            rideAlertIo.setSourceName(rideAlert.getSourceName());
            rideAlertIo.setSource(rideAlertIo.getSource());
            rideAlertIo.setDest(rideAlert.getDestPt());
            rideAlertIo.setDestName(rideAlert.getDestName());
            rideAlertIo.setStartTime(rideAlert.getStartTime());
            rideAlertIo.setEndTime(rideAlert.getEndTime());
            rideAlertIo.setGender(rideAlert.getGender());
            rideAlertIo.setVehicleType(rideAlert.getVehicleType());
        }

        return rideAlertIo;
    }

    private static void filterForTiming(Ride ride, List<RideAlert> rideAlerts)
    {
        if(rideAlerts != null)
        {
            Iterator<RideAlert> rideAlertIterator = rideAlerts.iterator();

            while (rideAlertIterator.hasNext())
            {
                RideAlert rideAlert = rideAlertIterator.next();

                if(ride.getStartDate() != null)
                {
                    Calendar rideCalendar = Calendar.getInstance();
                    rideCalendar.setTime(ride.getStartDate());

                    if(rideAlert.getStartTime() != null && rideAlert.getEndTime() != null)
                    {
                        Calendar alertStartCalendar = Calendar.getInstance();
                        alertStartCalendar.setTime(rideAlert.getStartTime());

                        Calendar alertEndCalendar = Calendar.getInstance();
                        alertEndCalendar.setTime(rideAlert.getEndTime());

                        int startHour = alertStartCalendar.get(Calendar.HOUR_OF_DAY);
                        int endHour = alertEndCalendar.get(Calendar.HOUR_OF_DAY);

                        int rideHour = rideCalendar.get(Calendar.HOUR_OF_DAY);

                        if(rideHour < startHour || rideHour > endHour)
                        {
                            rideAlertIterator.remove();
                        }
                    }
                }
            }
        }
    }
}
