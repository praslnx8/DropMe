package com.prasilabs.dropme.customs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.api.client.util.ArrayMap;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.utils.MarkerUtil;

import java.util.Map;

/**
 * Created by prasi on 12/12/15.
 */
public class MapLoader implements GoogleMap.OnInfoWindowClickListener
{
    private static final String TAG = MapLoader.class.getSimpleName();
    private MapView mapView;
    private Bundle savedInstanceState;
    private GoogleMap gMap;
    private MapMarkerClick mapMarkerClick;
    private boolean disableMapchangeListener;
    private Map<String, Marker> markerMap = new ArrayMap<>();
    private boolean isMapLoaded = false;

    public MapLoader(MapView mapView, Bundle savedInstanceState) {
        this.mapView = mapView;
        this.savedInstanceState = savedInstanceState;
    }

    public void loadMap(final MapLoaderCallBack mapLoaderCallBack) {
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap)
            {
                isMapLoaded = true;
                gMap = googleMap;
                //gMap.setMyLocationEnabled(true);
                mapLoaderCallBack.mapLoaded();

                if (ActivityCompat.checkSelfPermission(mapView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mapView.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //gMap.setMyLocationEnabled(true);
                }
            }
        });
    }

    public void moveToLoc(LatLng target)
    {
        if(gMap != null && target != null)
        {
            disableMapchangeListener = true;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(target, 15);
            gMap.animateCamera(cameraUpdate);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    disableMapchangeListener = false;
                }
            }, 3000);
        }
    }

    public void listenMapchange(final MapChangeCallBack mapChangeCallBack)
    {
        if(gMap != null)
        {
            gMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition)
                {
                    if(!disableMapchangeListener)
                    {
                        mapChangeCallBack.getLatLng(cameraPosition.target);
                    }
                }
            });
        }
    }

    public void addMarker(LatLng latLng, boolean isClear, int resourceId)
    {
        addMarker(null, latLng, isClear, resourceId);
    }

    public void addMarker(String id, LatLng latLng, int resourceId)
    {
        addMarker(id, latLng, false, resourceId);
    }

    public void addMarker(String id, LatLng latLng, boolean isClear, int resourceId)
    {
        addMarker(id, latLng, isClear, resourceId, 0);
    }

    public void addMarker(String id, LatLng latLng, boolean isClear, int resourceId, float dir) {
        if(latLng != null)
        {
            if (isClear)
            {
                gMap.clear();
                markerMap.clear();
            }

            Marker existingMarker = null;
            if(id != null)
            {
                existingMarker = markerMap.get(id);
            }

            if(existingMarker != null)
            {
                existingMarker.setPosition(latLng);
            }
            else
            {
                MarkerOptions markerOptions = new MarkerOptions();
                if(resourceId != 0)
                {
                    BitmapDrawable bitmapdraw=(BitmapDrawable) mapView.getContext().getResources().getDrawable(resourceId);
                    if(bitmapdraw != null)
                    {
                        Bitmap bitmap = bitmapdraw.getBitmap();
                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 70, 70, false);

                        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(resizedBitmap);
                        markerOptions.icon(icon);
                    }
                    else
                    {
                        ConsoleLog.w(TAG, "bitmapDrawable is null");
                    }
                }
                if (dir != 0) {
                    markerOptions.rotation(dir);
                }
                markerOptions.position(latLng);
                Marker marker = gMap.addMarker(markerOptions);
                animateMarker(marker, latLng, false);
                if(id != null)
                {
                    markerMap.put(id, marker);
                }
            }
        }
    }

    public void animateMarker(final Marker marker, final LatLng toPosition, final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = gMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;
        final LinearInterpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    public void removeMarker(String markerId)
    {
        Marker marker = markerMap.get(markerId);
        if(marker != null)
        {
            marker.remove();
        }
    }

    public LatLng getPointedLatLng()
    {
        if(gMap != null) {
            return gMap.getCameraPosition().target;
        }

        return null;
    }

    public void showDirection(LatLng sourceLatLng, LatLng destLatLng, boolean isClear)
    {
        if(isClear)
        {
            clearPolyLine();
        }
        removeMarker(MarkerUtil.SOURCE_MARKER_KEY);
        removeMarker(MarkerUtil.DEST_MARKER_KEY);
        addMarker(MarkerUtil.SOURCE_MARKER_KEY, sourceLatLng, MarkerUtil.SOURCE_MARKER);
        addMarker(MarkerUtil.DEST_MARKER_KEY, destLatLng, MarkerUtil.DEST_MARKER);

        new DirectionManager().showDirection(this, sourceLatLng, destLatLng);
    }

    public void addPolyLine(PolylineOptions polylineOptions)
    {
        if(polylineOptions != null && gMap != null)
        {
            gMap.addPolyline(polylineOptions);
        }
    }

    public void clearPolyLine()
    {
        if(gMap != null)
        {
            PolylineOptions polylineOptions = new PolylineOptions();
            gMap.addPolyline(polylineOptions);
        }
    }

    public void listenToMarkerClick(MapMarkerClick mapMarkerClick)
    {
        this.mapMarkerClick = mapMarkerClick;
    }

    @Override
    public void onInfoWindowClick(Marker marker)
    {
        String id = marker.getId();

        if(mapMarkerClick != null)
        {
            mapMarkerClick.markerClicked(id);
        }
    }

    public boolean isMapLoaded() {
        return isMapLoaded;
    }

    public interface MapLoaderCallBack
    {
        void mapLoaded();
    }

    public interface MapChangeCallBack
    {
        void getLatLng(LatLng latLng);
    }

    public interface MapMarkerClick
    {
        void markerClicked(String id);
    }
}
