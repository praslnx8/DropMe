package com.prasilabs.dropme.services.location;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.maps.model.LatLng;
import com.prasilabs.dropme.constants.BroadCastConstant;
import com.prasilabs.dropme.constants.LocationConstant;
import com.prasilabs.dropme.core.CoreApp;
import com.prasilabs.dropme.customs.LocalPreference;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.modelengines.HomeGeoModelEngine;
import com.prasilabs.dropme.utils.LocationUtils;
import com.prasilabs.dropme.utils.ViewUtil;

/**
 * Created by prasi on 8/6/15.
 */
public class DropMeLocatioListener implements LocationListener
{
    private static final String TAG = DropMeLocatioListener.class.getSimpleName();

    private static DropMeLocatioListener instance = new DropMeLocatioListener();
    private LocationManager lm;
    private Location location;
    private Context context;

    private DropMeLocatioListener() {
    }

    public static DropMeLocatioListener getInstance()
    {
        return instance;
    }

    public static void informLocation(Context context, boolean isCallServer) {
        HomeGeoModelEngine.getInstance().locationChanged(isCallServer);
        Intent locationIntent = new Intent();
        locationIntent.setAction(BroadCastConstant.LOCATION_REFRESH_CONSTANT);
        LocalBroadcastManager.getInstance(context).sendBroadcast(locationIntent);
    }

    public static LatLng getLatLng(Context context) {
        LatLng latLng = LocalPreference.getLocationFromPrefs(context, LocationConstant.CURRENT_LOC_STR);

        return latLng;
    }

    public boolean isLocationEnabled(Context context)
    {
        this.context = context;

        if(lm == null)
        {
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }

        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void registerLoc(Context context)
    {
        this.context = context;

        if(lm == null)
        {
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }

        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            if (checkLocationPermission(context))
            {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 100, this);
                location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        else
        {
            ViewUtil.t(context, "Please enable GPS");
            Intent intnt = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intnt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intnt);
        }

        final Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(false);
        criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);
        if(checkLocationPermission(context))
        {
            String best = lm.getBestProvider(criteria, true);
            lm.requestLocationUpdates(best, 2000, 50, this);
        }

        if(location != null)
        {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            LocationUtils.checkAndSendLoc(context, latLng, null);

            stopLocationUpdates(context);
        }


    }

    @Override
    public void onLocationChanged(Location location)
    {
        if(context == null)
        {
            context = CoreApp.getAppContext();
        }

        if(location != null && context != null)
        {
            this.location = location;
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            LocalPreference.storeLocation(context, latLng, LocationConstant.CURRENT_LOC_STR);
            informLocation(context, true);

            stopLocationUpdates(context);
        }

    }

    public void stopLocationUpdates(Context context)
    {
        try
        {
            if(checkLocationPermission(context))
            {
                lm.removeUpdates(this);
            }
        }
        catch (Exception e)
        {
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    @Override
    public void onProviderEnabled(String provider)
    {
        if (lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER))
        {
            Intent intent = new Intent();
            intent.setAction(BroadCastConstant.LOCATION_ENABLED_CONSTANT);
            LocalBroadcastManager.getInstance(CoreApp.getAppContext()).sendBroadcast(intent);

            if(checkLocationPermission(CoreApp.getAppContext()))
            {
                lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 2000, 50, this);
                location = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            }

            final Criteria criteria = new Criteria();
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            criteria.setAltitudeRequired(false);
            criteria.setSpeedAccuracy(Criteria.ACCURACY_LOW);
            if(checkLocationPermission(context))
            {
                String best = lm.getBestProvider(criteria, true);
                lm.requestLocationUpdates(best, 2000, 50, this);
            }

            if(location != null)
            {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                //LatLng oldLoc = LocalPreference.getLocationFromPrefs(context, MyConstant.CURRENT_LATLNG_TEXT);
                LocalPreference.storeLocation(context, latLng, LocationConstant.CURRENT_LOC_STR);
                informLocation(context, true);
            }
        }
        else
        {
            ConsoleLog.i(TAG, "Gps is disabled");
        }
    }

    @Override
    public void onProviderDisabled(String provider)
    {
        if(checkLocationPermission(CoreApp.getAppContext()))
        {
            lm.removeUpdates(this);
        }
    }

    private boolean checkLocationPermission(Context context)
    {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}