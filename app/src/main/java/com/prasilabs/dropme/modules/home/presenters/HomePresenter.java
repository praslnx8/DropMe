package com.prasilabs.dropme.modules.home.presenters;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.google.android.gms.maps.model.LatLng;
import com.prasilabs.dropme.constants.BroadCastConstant;
import com.prasilabs.dropme.constants.LocationConstant;
import com.prasilabs.dropme.core.CorePresenter;
import com.prasilabs.dropme.customs.LocalPreference;
import com.prasilabs.dropme.modelengines.HomeGeoModelEngine;
import com.prasilabs.dropme.pojo.MarkerInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prasi on 27/5/16.
 */
public class HomePresenter extends CorePresenter
{
    private MapChange mapChange;
    private static final String TAG = HomePresenter.class.getSimpleName();
    private List<MarkerInfo> markerInfoList = new ArrayList<>();

    public static HomePresenter newInstance(MapChange mapChange)
    {
        return new HomePresenter(mapChange);
    }

    private  HomePresenter(MapChange mapChange)
    {
        this.mapChange = mapChange;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadCastConstant.LOCATION_REFRESH_CONSTANT);
        registerReciever(intentFilter);
    }

    private void listenToMap(LatLng latLng)
    {
        HomeGeoModelEngine.getInstance().listenToGeoLoc(latLng, new HomeGeoModelEngine.GeoCallBack() {
            @Override
            public void getMarker(MarkerInfo markerInfo)
            {
                if(checkAlreadyExist(markerInfo))
                {
                    mapChange.moveMarker(markerInfo);
                }
                else
                {
                    markerInfoList.add(markerInfo);
                    mapChange.addMarker(markerInfo);
                }
            }

            @Override
            public void removeMarker(MarkerInfo markerInfo)
            {
                mapChange.removeMarker(markerInfo);
            }
        });
    }

    private boolean checkAlreadyExist(MarkerInfo markerInfo)
    {
        boolean isAlreadyExist = false;

        for(MarkerInfo markerInfo1 : markerInfoList)
        {
            if(markerInfo1.getId() == markerInfo.getId() && markerInfo.getUserOrVehicle().equals(markerInfo1.getUserOrVehicle()))
            {
                return isAlreadyExist = true;
            }
        }

        return isAlreadyExist;
    }

    @Override
    protected void broadCastRecieved(Context context, Intent intent)
    {
        if(intent.getAction().equals(BroadCastConstant.LOCATION_REFRESH_CONSTANT))
        {
            LatLng latLng = LocalPreference.getLocationFromPrefs(context, LocationConstant.CURRENT_LOC_STR);
            if(latLng != null)
            {
                listenToMap(latLng);
            }
        }
    }


    public interface MapChange
    {
        void addMarker(MarkerInfo markerInfo);

        void moveMarker(MarkerInfo markerInfo);

        void removeMarker(MarkerInfo markerInfo);
    }
}
