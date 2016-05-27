package com.prasilabs.dropme.services.firebase;

import com.firebase.client.Firebase;
import com.firebase.geofire.GeoFire;

/**
 * Created by prasi on 27/5/16.
 */
public class FireBaseConfig
{
    private static final String FIREBASE_URL = "";
    private static Firebase firebase;
    private static GeoFire geoFire;

    public static Firebase getFireBase()
    {
        if(firebase == null)
        {
            firebase = new Firebase(FIREBASE_URL);
        }

        return firebase;
    }

    public static GeoFire getGeoFire()
    {
        if(geoFire == null)
        {
            geoFire = new GeoFire(FireBaseConfig.getFireBase());
        }

        return geoFire;
    }
}
