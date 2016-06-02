package com.prasilabs.dropme.backend.services.geofire;

import com.firebase.client.Firebase;
import com.firebase.geofire.GeoFire;
import com.prasilabs.constants.AuthConstants;
import com.prasilabs.dropme.backend.debug.ConsoleLog;
import com.prasilabs.util.DataUtil;

/**
 * Created by prasi on 31/5/16.
 */
public class GeoFireManager
{
    private static final String TAG = GeoFireManager.class.getSimpleName();
    private static GeoFire geoFire;

    public static GeoFire getGeoFire()
    {
        if(geoFire == null)
        {
            geoFire = new GeoFire(new Firebase(AuthConstants.FIREBASE_URL));
        }

        return geoFire;
    }

    public static boolean removeGeoPoint(String key)
    {
        boolean success = false;
        if(!DataUtil.isEmpty(key))
        {
            getGeoFire().removeLocation(key);
            success = true;
        }
        else
        {
            ConsoleLog.s(TAG, "key is empty to remove");
        }

        return success;
    }
}
