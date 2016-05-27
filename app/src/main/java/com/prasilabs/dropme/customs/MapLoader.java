package com.prasilabs.dropme.customs;

import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by prasi on 12/12/15.
 */
public class MapLoader {
    private MapView mapView;
    private Bundle savedInstanceState;
    private GoogleMap gMap;
    private boolean disableMapchangeListener;

    public MapLoader(MapView mapView, Bundle savedInstanceState) {
        this.mapView = mapView;
        this.savedInstanceState = savedInstanceState;
    }

    public void loadMap(final MapLoaderCallBack mapLoaderCallBack) {
        mapView.onCreate(savedInstanceState);

        gMap = mapView.getMap();
        /*mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap)
            {
                gMap = googleMap;
                //gMap.setMyLocationEnabled(true);
                mapLoaderCallBack.mapLoaded();
            }
        });*/
        /*if (ActivityCompat.checkSelfPermission(mapView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mapView.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
        }*/

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

    public void addMarker(LatLng latLng, boolean isClear)
    {
        if(latLng != null) {
            if (isClear) {
                gMap.clear();
            }

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            gMap.addMarker(markerOptions);
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
            gMap.clear();
        }

        new DirectionManager().showDirection(this, sourceLatLng, destLatLng);
    }

    public void addPolyLine(PolylineOptions polylineOptions)
    {
        if(polylineOptions != null && gMap != null) {
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
