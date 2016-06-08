package com.prasilabs.dropme.backend.logicEngines;

import com.google.api.server.spi.auth.common.User;
import com.google.appengine.api.datastore.GeoPt;
import com.googlecode.objectify.Key;
import com.prasilabs.dropme.backend.core.CoreLogicEngine;
import com.prasilabs.dropme.backend.datastore.DropMeUser;
import com.prasilabs.dropme.backend.datastore.LocationShare;
import com.prasilabs.dropme.backend.datastore.UserLocation;
import com.prasilabs.dropme.backend.db.OfyService;
import com.prasilabs.dropme.backend.io.ApiResponse;

import java.util.Date;

/**
 * Created by prasi on 8/6/16.
 */
public class UserLocationLogicEngine extends CoreLogicEngine
{
    private static UserLocationLogicEngine instance;

    private UserLocationLogicEngine(){}

    public static UserLocationLogicEngine getInstance()
    {
        if(instance == null)
        {
            instance = new UserLocationLogicEngine();
        }

        return instance;
    }

    public ApiResponse updateUserLocation(User user, String deviceId, GeoPt currentLoc)
    {
        ApiResponse apiResponse = new ApiResponse();


        DropMeUser dropMeUser = DropMeUserLogicEngine.getInstance().getDropMeUser(user.getEmail());

        UserLocation existingLocation = OfyService.ofy().load().type(UserLocation.class).filter(UserLocation.USER_ID_STR, dropMeUser.getId()).filter(UserLocation.DEVICE_ID_STR, deviceId).first().now();

        if(existingLocation != null)
        {
            existingLocation.setCurrentLoc(currentLoc);
            existingLocation.setModified(new Date(System.currentTimeMillis()));

            Key<UserLocation> userLocationKey = OfyService.ofy().save().entity(existingLocation).now();
            apiResponse.setStatus(true);
            apiResponse.setId(userLocationKey.getId());
        }
        else
        {
            UserLocation userLocation = new UserLocation();

            userLocation.setUserId(dropMeUser.getId());
            userLocation.setDeviceId(deviceId);
            userLocation.setCurrentLoc(currentLoc);
            userLocation.setCreated(new Date(System.currentTimeMillis()));
            userLocation.setModified(new Date(System.currentTimeMillis()));

            Key<UserLocation> userLocationKey = OfyService.ofy().save().entity(userLocation).now();
            apiResponse.setStatus(true);
            apiResponse.setId(userLocationKey.getId());
        }

        return apiResponse;
    }

    public ApiResponse shareLocation(User user, long recieverID, String deviceId)
    {
        ApiResponse apiResponse = new ApiResponse();

        DropMeUser dropMeUser = DropMeUserLogicEngine.getInstance().getDropMeUser(user.getEmail());
        DropMeUser reciever = DropMeUserLogicEngine.getInstance().getDropMeUser(recieverID);

        if(dropMeUser != null && reciever != null)
        {
            LocationShare existingLocShare = OfyService.ofy().load().type(LocationShare.class).filter(LocationShare.DEVICE_ID_STR, deviceId).filter(LocationShare.USER_ID_STR, dropMeUser.getId()).filter(LocationShare.RECIEVER_ID_STR, reciever.getId()).first().now();

            if(existingLocShare != null)
            {
                existingLocShare.setModifiedTime(new Date(System.currentTimeMillis()));
                existingLocShare.setInactive(false);

                Key<LocationShare> locationShareKey = OfyService.ofy().save().entity(existingLocShare).now();
                apiResponse.setStatus(true);
                apiResponse.setId(locationShareKey.getId());
            }
            else
            {
                LocationShare locationShare = new LocationShare();
                locationShare.setUserId(dropMeUser.getId());
                locationShare.setDeviceId(deviceId);
                locationShare.setRecieverId(reciever.getId());
                locationShare.setCreatedTime(new Date(System.currentTimeMillis()));
                locationShare.setModifiedTime(new Date(System.currentTimeMillis()));

                Key<LocationShare> locationShareKey = OfyService.ofy().save().entity(locationShare).now();
                apiResponse.setStatus(true);
                apiResponse.setId(locationShareKey.getId());
            }
        }

        return apiResponse;
    }

}
