package com.prasilabs.dropme.backend.services.geofire;

import com.firebase.client.Firebase;
import com.firebase.geofire.GeoFire;
import com.prasilabs.constants.AuthConstants;

/**
 * Created by prasi on 31/5/16.
 */
public class GeoFireManager
{
    private static GeoFire geoFire;

    public static GeoFire getGeoFire()
    {
        if(geoFire == null)
        {
            geoFire = new GeoFire(new Firebase(AuthConstants.FIREBASE_URL));
        }

        return geoFire;
    }

    public static void removeGeoPoint(String key)
    {

    }
}
