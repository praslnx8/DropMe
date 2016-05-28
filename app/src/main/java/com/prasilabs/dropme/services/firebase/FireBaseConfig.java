package com.prasilabs.dropme.services.firebase;

import com.firebase.client.Firebase;
import com.firebase.geofire.GeoFire;
import com.prasilabs.dropme.core.CoreApp;

/**
 * Created by prasi on 27/5/16.
 */
public class FireBaseConfig
{
    private static final String FIREBASE_URL = "https://prasilabs-dropme.firebaseio.com";
    private static Firebase firebase;
    private static GeoFire geoFire;

    public static Firebase getFireBase()
    {
        if(firebase == null)
        {
            Firebase.setAndroidContext(CoreApp.getAppContext());
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
