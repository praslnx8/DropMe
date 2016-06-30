package com.prasilabs.dropme.utils;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.prasilabs.dropme.backend.dropMeApi.model.GeoPt;
import com.prasilabs.dropme.constants.LocationConstant;
import com.prasilabs.dropme.customs.LocalPreference;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.services.location.DropMeLocatioListener;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by prasi on 31/5/16.
 */
public class LocationUtils
{
    public static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final String DISTANCE_KM_POSTFIX = "KM";
    private static final String DISTANCE_M_POSTFIX = "M";
    private static final String TAG = LocationUtils.class.getSimpleName();

    public static GeoPt convertToGeoPt(LatLng latLng)
    {

        GeoPt geoPt = null;
        if(latLng != null)
        {
            geoPt = new GeoPt();
            Double lat = latLng.latitude;
            Double lon = latLng.longitude;
            geoPt.setLatitude(lat.floatValue());
            geoPt.setLongitude(lon.floatValue());
        }

        return geoPt;
    }

    public static LatLng convertToLatLng(GeoPt geoPt)
    {
        if(geoPt != null)
        {

            Float lat = geoPt.getLatitude();
            Float lon = geoPt.getLongitude();

            LatLng latLng = new LatLng(lat.doubleValue(), lon.doubleValue());

            return latLng;
        }

        return null;
    }

    public static String formatDistanceBetween(LatLng point1, LatLng point2) {
        if (point1 == null || point2 == null) {
            return "";
        }

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        double distance = Math.round(SphericalUtil.computeDistanceBetween(point1, point2));

        // Adjust to KM if M goes over 1000 (see javadoc of method for note
        // on only supporting metric)
        if (distance >= 100) {
            numberFormat.setMaximumFractionDigits(1);
            return numberFormat.format(distance / 1000) + DISTANCE_KM_POSTFIX;
        }
        return numberFormat.format(distance) + DISTANCE_M_POSTFIX;
    }

    public static void getLocationFromLatLng(final Context context, LatLng latLng, final GetLocationNameCallBack getLocationNameCallBack)
    {
        new AsyncTask<LatLng, Void, String>()
        {

            @Override
            protected String doInBackground(LatLng... params)
            {
                LatLng latLng = params[0];

                String locationName = null;
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                try
                {
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                    if(addressList != null && addressList.size() > 0)
                    {
                        Address address = addressList.get(0);

                        locationName = address.getLocality();
                    }
                } catch (Exception e)
                {
                    ConsoleLog.e(e);
                }

                return locationName;
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);

                if(getLocationNameCallBack != null)
                {
                    getLocationNameCallBack.getLocationName(s);
                }
            }
        }.execute(latLng);
    }

    public static void checkAndSendLoc(Context context, LatLng latLngLocation, String locName) {
        LatLng oldLatLng = LocalPreference.getLocationFromPrefs(context, LocationConstant.CURRENT_LOC_STR);
        LatLng aggrOldLatLng = LocalPreference.getLocationFromPrefs(context, LocationConstant.AGGREGATE_CURRENT_LOC_STR);

        double distance = 0.0;
        double aggrDistance = 0.0;
        if (oldLatLng != null) {
            distance = Math.round(SphericalUtil.computeDistanceBetween(oldLatLng, latLngLocation));
        }
        if (aggrOldLatLng != null) {
            aggrDistance = Math.round(SphericalUtil.computeDistanceBetween(aggrOldLatLng, latLngLocation));
        }

        if (locName != null) {
            LocalPreference.saveAppDataInShared(context, LocationConstant.LOC_NAME_STR, locName);
        }
        if (aggrOldLatLng == null || aggrDistance > 400) {
            ConsoleLog.i(TAG, "sending loc to server");
            LocalPreference.storeLocation(context, latLngLocation, LocationConstant.AGGREGATE_CURRENT_LOC_STR);
            LocalPreference.storeLocation(context, latLngLocation, LocationConstant.CURRENT_LOC_STR);
            DropMeLocatioListener.informLocation(context, true);
        } else if (oldLatLng == null || distance > 100) {
            ConsoleLog.i(TAG, "sending loc only for geo fire");
            LocalPreference.storeLocation(context, latLngLocation, LocationConstant.CURRENT_LOC_STR);
            DropMeLocatioListener.informLocation(context, false);
        }
    }

    public static void askLocationRequest(final Activity activity) {
        ConsoleLog.i(TAG, "ask location called");
        GoogleApiClient.ConnectionCallbacks connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                ConsoleLog.i(TAG, "onconnected");
            }

            @Override
            public void onConnectionSuspended(int i) {
                ConsoleLog.i(TAG, " suspended");
            }
        };

        GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                ConsoleLog.w(TAG, "failed");
                ConsoleLog.i(TAG, " connection result is " + connectionResult.getErrorMessage() + " code " + connectionResult.getErrorCode());
            }
        };

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(onConnectionFailedListener).build();

        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    public interface GetLocationNameCallBack
    {
        void getLocationName(String location);
    }
}
