package com.prasilabs.dropme.modules.home.presenters;

import android.content.Context;
import android.content.Intent;

import com.firebase.client.FirebaseError;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.prasilabs.dropme.core.CorePresenter;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.pojo.MarkerInfo;
import com.prasilabs.dropme.services.firebase.FireBaseConfig;

/**
 * Created by prasi on 27/5/16.
 */
public class HomePresenter extends CorePresenter
{

    private static final String TAG = HomePresenter.class.getSimpleName();

    public static HomePresenter newInstance()
    {
        return new HomePresenter();
    }

    @Override
    protected void broadCastRecieved(Context context, Intent intent) {

    }

    public void listentToGeo(LatLng latLng)
    {
        GeoQuery geoQuery = FireBaseConfig.getGeoFire().queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), 0.5);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location)
            {

            }

            @Override
            public void onKeyExited(String key)
            {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location)
            {

            }

            @Override
            public void onGeoQueryReady()
            {
                ConsoleLog.i(TAG, "query ready");
            }

            @Override
            public void onGeoQueryError(FirebaseError error)
            {

            }
        });
    }

    public interface MapChange
    {
        void addMarker(MarkerInfo markerInfo, LatLng latLng);

        void moveMarker(MarkerInfo markerInfo, LatLng latLng);

        void removeMarker(MarkerInfo markerInfo);
    }
}
