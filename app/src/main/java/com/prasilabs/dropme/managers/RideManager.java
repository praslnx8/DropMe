package com.prasilabs.dropme.managers;

import android.content.Context;

import com.google.api.client.util.DateTime;
import com.prasilabs.dropme.backend.dropMeApi.model.GeoPt;
import com.prasilabs.dropme.backend.dropMeApi.model.RideInput;
import com.prasilabs.dropme.constants.PojoConstants;
import com.prasilabs.dropme.customs.LocalPreference;
import com.prasilabs.dropme.utils.LocationUtils;
import com.prasilabs.util.DataUtil;

/**
 * Created by prasi on 28/5/16.
 */
public class RideManager
{
    private static final String TAG = RideManager.class.getSimpleName();

    public static boolean saveRideLite(Context context, RideInput rideInput)
    {
        boolean isSuccess = false;

        if(rideInput != null)
        {
            LocalPreference.saveLoginDataInShared(context, PojoConstants.RideConstant.ID_STR, rideInput.getId());
            LocalPreference.saveLoginDataInShared(context, PojoConstants.RideConstant.USER_ID_STR, rideInput.getUserId());
            LocalPreference.saveLoginDataInShared(context, PojoConstants.RideConstant.DEVICE_ID_STR, rideInput.getDeviceId());
            LocalPreference.saveLoginDataInShared(context, PojoConstants.RideConstant.VEHICEL_ID_STR, rideInput.getVehicleId());
            LocalPreference.storeLocation(context, LocationUtils.convertToLatLng(rideInput.getSourceLoc()), PojoConstants.RideConstant.SOURCE_LOC_STR);
            LocalPreference.saveLoginDataInShared(context, PojoConstants.RideConstant.DEST_LOC_NAME_STR, rideInput.getDestLocName());
            LocalPreference.storeLocation(context, LocationUtils.convertToLatLng(rideInput.getDestLoc()), PojoConstants.RideConstant.DEST_LOC_STR);
            LocalPreference.storeLocation(context, LocationUtils.convertToLatLng(rideInput.getCurrentLoc()), PojoConstants.RideConstant.CURRENT_LOC_STR);
            LocalPreference.saveLoginDataInShared(context, PojoConstants.RideConstant.IS_CLOSED_STR, rideInput.getClosed());
            LocalPreference.saveLoginDataInShared(context, PojoConstants.RideConstant.FARE_PER_KM_STR, rideInput.getFarePerKm());
            LocalPreference.saveLoginDateDataInShared(context, PojoConstants.RideConstant.EXPIRY_DATE_STR, rideInput.getExpiryDate());
            LocalPreference.saveLoginDateDataInShared(context, PojoConstants.RideConstant.START_DATE_STR, rideInput.getStartDate());
            LocalPreference.saveLoginDateDataInShared(context, PojoConstants.RideConstant.CLOSED_DATE_STR, rideInput.getClosedDate());
        }
        else
        {
            LocalPreference.saveLoginDataInShared(context, PojoConstants.RideConstant.ID_STR, 0L);
        }

        return isSuccess;
    }

    public static RideInput getRideLite(Context context)
    {
        RideInput rideInput = null;


        long id = LocalPreference.getLoginDataFromShared(context, PojoConstants.RideConstant.ID_STR, 0L);
        long userId = LocalPreference.getLoginDataFromShared(context, PojoConstants.RideConstant.USER_ID_STR, 0L);
        String deviceID = LocalPreference.getLoginDataFromShared(context, PojoConstants.RideConstant.DEVICE_ID_STR, null);
        long vehicleID = LocalPreference.getLoginDataFromShared(context, PojoConstants.RideConstant.VEHICEL_ID_STR, 0L);
        GeoPt source = LocationUtils.convertToGeoPt(LocalPreference.getLocationFromPrefs(context, PojoConstants.RideConstant.SOURCE_LOC_STR));
        GeoPt dest = LocationUtils.convertToGeoPt(LocalPreference.getLocationFromPrefs(context, PojoConstants.RideConstant.DEST_LOC_STR));
        GeoPt currentLoc = LocationUtils.convertToGeoPt(LocalPreference.getLocationFromPrefs(context, PojoConstants.RideConstant.CURRENT_LOC_STR));
        String destLocName = LocalPreference.getLoginDataFromShared(context, PojoConstants.RideConstant.DEST_LOC_NAME_STR, null);
        boolean isClosed = LocalPreference.getLoginDataFromShared(context, PojoConstants.RideConstant.IS_CLOSED_STR, false);
        int fare = LocalPreference.getLoginDataFromShared(context, PojoConstants.RideConstant.FARE_PER_KM_STR, 0);
        DateTime expiryDate = LocalPreference.getLoginDateDataFromShared(context, PojoConstants.RideConstant.EXPIRY_DATE_STR);
        DateTime startDate = LocalPreference.getLoginDateDataFromShared(context, PojoConstants.RideConstant.START_DATE_STR);
        DateTime closedDate = LocalPreference.getLoginDateDataFromShared(context, PojoConstants.RideConstant.CLOSED_DATE_STR);

        if(!DataUtil.isEmpty(id) && expiryDate != null && expiryDate.getValue() > System.currentTimeMillis() && !isClosed)
        {
            rideInput = new RideInput();
            rideInput.setId(id);
            rideInput.setUserId(userId);
            rideInput.setDeviceId(deviceID);
            rideInput.setVehicleId(vehicleID);
            rideInput.setSourceLoc(source);
            rideInput.setDestLoc(dest);
            rideInput.setClosed(isClosed);
            rideInput.setFarePerKm(fare);
            rideInput.setCurrentLoc(currentLoc);
            rideInput.setDestLocName(destLocName);
            rideInput.setExpiryDate(expiryDate);
            rideInput.setStartDate(startDate);
            rideInput.setClosedDate(closedDate);
        }

        return rideInput;
    }
}
