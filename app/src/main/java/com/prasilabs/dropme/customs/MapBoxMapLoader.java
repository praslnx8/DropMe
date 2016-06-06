package com.prasilabs.dropme.customs;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.google.api.client.util.ArrayMap;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.prasilabs.dropme.debug.ConsoleLog;

import java.util.Map;

/**
 * Created by prasi on 12/12/15.
 */
public class MapBoxMapLoader
{
    public static final String MAPBOX_ACCESS_TOKEN = "pk.eyJ1IjoicHJhc2xueDgiLCJhIjoiY2lvcjltMXV3MDAzZnUwbTgxcWVvMjAyayJ9.bYEuK4yO9sxedj3Ler19Kg";

    private static final String TAG = MapBoxMapLoader.class.getSimpleName();
    private MapView mapView;
    private Bundle savedInstanceState;
    private MapboxMap gMap;
    private boolean disableMapchangeListener;
    private Map<String, Marker> markerMap = new ArrayMap<>();

    public MapBoxMapLoader(MapView mapView, Bundle savedInstanceState) {
        this.mapView = mapView;
        this.savedInstanceState = savedInstanceState;
    }

    public void loadMap(final MapLoaderCallBack mapLoaderCallBack)
    {
        //mapView.setAccessToken(MAPBOX_ACCESS_TOKEN);
        //mapView.setStyle(Style.LIGHT);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap)
            {
                ConsoleLog.i(TAG, "map ready");
                gMap = mapboxMap;

                mapLoaderCallBack.mapLoaded();
            }
        });
    }

    public void moveToLoc(com.google.android.gms.maps.model.LatLng target)
    {
        if(gMap != null && target != null)
        {
            LatLng mapBoxLatLng = new LatLng(target.latitude, target.longitude);
            disableMapchangeListener = true;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mapBoxLatLng, 15);
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
            gMap.setOnCameraChangeListener(new MapboxMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(com.mapbox.mapboxsdk.camera.CameraPosition position) {
                    if(!disableMapchangeListener)
                    {
                        mapChangeCallBack.getLatLng(position.target);
                    }
                }
            });
        }
    }


    public void addMarker(com.google.android.gms.maps.model.LatLng latLng, boolean isClear, int resourceId)
    {
        addMarker(null, latLng, isClear, resourceId);
    }

    public void addMarker(String id, com.google.android.gms.maps.model.LatLng latLng, int resourceId)
    {
        addMarker(id, latLng, false, resourceId);
    }

    public void addMarker(String id, com.google.android.gms.maps.model.LatLng latLng, boolean isClear, int resourceId)
    {
        if(latLng != null)
        {
            LatLng mapBoxLatLng = new LatLng(latLng.latitude, latLng.longitude);

            if (isClear)
            {
                //TODOgMap.clear();
                markerMap.clear();
            }

            Marker existingMarker = null;
            if(id != null)
            {
                existingMarker = markerMap.get(id);
            }

            if(existingMarker != null)
            {
                existingMarker.setPosition(mapBoxLatLng);
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
                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);


                        Icon icon = IconFactory.recreate(id, resizedBitmap);
                        markerOptions.icon(icon);
                    }
                    else
                    {
                        ConsoleLog.w(TAG, "bitmapDrawable is null");
                    }
                }


                markerOptions.position(mapBoxLatLng);
                Marker marker = gMap.addMarker(markerOptions);
                if(id != null)
                {
                    markerMap.put(id, marker);
                }
            }
        }
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

    public void showDirection(com.google.android.gms.maps.model.LatLng sourceLatLng, com.google.android.gms.maps.model.LatLng destLatLng, boolean isClear)
    {
        ConsoleLog.i(TAG, "show direction called");
        if(isClear)
        {
            clearPolyLine();
        }

        MapBoxDirectionManager.showDirection(mapView.getContext(), gMap, sourceLatLng, destLatLng);
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

    public interface MapLoaderCallBack
    {
        void mapLoaded();
    }

    public interface MapChangeCallBack
    {
        void getLatLng(LatLng latLng);
    }
}
