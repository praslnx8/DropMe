package com.prasilabs.dropme.services.location;

import android.Manifest;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.utils.LocationUtils;

import java.util.concurrent.TimeUnit;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DropMeGLocationService extends IntentService
{
    public static final String ACTION_REQUEST_LOCATION = "request_location";
    public static final String ACTION_LOCATION_UPDATED = "location_updated";
    private static final String TAG = DropMeLocatioListener.class.getSimpleName();
    private static boolean isRunning = false;

    public DropMeGLocationService() {
        super(TAG);
    }

    public static void start(Context context)
    {
        if (!isRunning)
        {
            Intent intent = new Intent(context, DropMeGLocationService.class);
            intent.setAction(DropMeGLocationService.ACTION_REQUEST_LOCATION);
            context.startService(intent);
        }
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        ConsoleLog.i(TAG, "Requesting location started");
        DropMeLocatioListener.getInstance().registerLoc(this);
    }

    /**
     * Called when a location update is requested
     */
    private void requestLocationInternal() {
        ConsoleLog.i(TAG, ACTION_REQUEST_LOCATION);
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).build();

        // It's OK to use blockingConnect() here as we are running in an
        // IntentService that executes work on a separate (background) thread.
        ConnectionResult connectionResult = googleApiClient.blockingConnect(10, TimeUnit.SECONDS);

        if (connectionResult.isSuccess() && googleApiClient.isConnected()) {

            Intent locationUpdatedIntent = new Intent(this, DropMeGLocationService.class);
            locationUpdatedIntent.setAction(ACTION_LOCATION_UPDATED);

            // Send last known location out first if available
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ConsoleLog.w(TAG, "location permision is not enabled");
                return;
            }
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (location != null)
            {
                Intent lastLocationIntent = new Intent(locationUpdatedIntent);
                lastLocationIntent.putExtra(FusedLocationProviderApi.KEY_LOCATION_CHANGED, location);
                startService(lastLocationIntent);
            }

            // Request new location
            LocationRequest mLocationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY).setFastestInterval(60000);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, PendingIntent.getService(this, 0, locationUpdatedIntent, 0));

            googleApiClient.disconnect();
        }
        else
        {
            ConsoleLog.w(TAG, "error code : " + connectionResult.getErrorCode());
            ConsoleLog.w(TAG, "error message : " + connectionResult.getErrorMessage());
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent != null ? intent.getAction() : null;
        if (ACTION_REQUEST_LOCATION.equals(action))
        {
            ConsoleLog.i(TAG, "intent received");
            if (checkLocationPermission())
            {
                requestLocationInternal();
            }
        }
        else if (ACTION_LOCATION_UPDATED.equals(action))
        {
            locationUpdated(intent);
        }
    }

    /**
     * Called when the location has been updated
     */
    private void locationUpdated(Intent intent)
    {

        ConsoleLog.i(TAG, "location update recieved");

        Location location = intent.getParcelableExtra(FusedLocationProviderApi.KEY_LOCATION_CHANGED);

        if (location != null)
        {
            final LatLng latLngLocation = new LatLng(location.getLatitude(), location.getLongitude());

            LocationUtils.getLocationFromLatLng(this, latLngLocation, new LocationUtils.GetLocationNameCallBack() {
                @Override
                public void getLocationName(String location)
                {
                    LocationUtils.checkAndSendLoc(DropMeGLocationService.this, latLngLocation, location);
                }
            });
        }
        else
        {
            ConsoleLog.w(TAG, "location is null");
        }
    }

    private boolean checkLocationPermission()
    {
        return ActivityCompat.checkSelfPermission(DropMeGLocationService.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        isRunning = false;
    }
}
