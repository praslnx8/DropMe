package com.prasilabs.dropme.modules.home.presenters;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.google.android.gms.maps.model.LatLng;
import com.prasilabs.dropme.constants.BroadCastConstant;
import com.prasilabs.dropme.core.CorePresenter;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.modelengines.HomeGeoModelEngine;
import com.prasilabs.dropme.pojo.MarkerInfo;
import com.prasilabs.dropme.services.location.DropMeLocatioListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prasi on 27/5/16.
 */
public class HomePresenter extends CorePresenter
{
    private static final String TAG = HomePresenter.class.getSimpleName();
    private MapChange mapChange;
    private List<MarkerInfo> markerInfoList = new ArrayList<>();

    public HomePresenter(MapChange mapChange)
    {
        this.mapChange = mapChange;
    }

    private void listenToMap(LatLng latLng)
    {
        ConsoleLog.i(TAG, "listen to map triggered");
        HomeGeoModelEngine.getInstance().listenToHomeGeoLoc(latLng, new HomeGeoModelEngine.GeoCallBack()
        {
            @Override
            public void getMarker(MarkerInfo markerInfo)
            {
                if(checkAlreadyExist(markerInfo))
                {
                    if(mapChange != null)
                    {
                        mapChange.moveMarker(markerInfo);
                    }
                }
                else
                {
                    markerInfoList.add(markerInfo);
                    if(mapChange != null)
                    {
                        mapChange.addMarker(markerInfo);
                    }
                }
            }

            @Override
            public void removeMarker(MarkerInfo markerInfo)
            {
                if(mapChange != null)
                {
                    mapChange.removeMarker(markerInfo);
                }
            }
        });
    }

    private boolean checkAlreadyExist(MarkerInfo markerInfo)
    {
        for(MarkerInfo markerInfo1 : markerInfoList)
        {
            if(markerInfo1.getKey().equals(markerInfo.getKey()) && markerInfo.getUserOrVehicle().equals(markerInfo1.getUserOrVehicle()))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onCreateCalled()
    {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadCastConstant.LOCATION_REFRESH_CONSTANT);
        registerReciever(intentFilter);
    }

    @Override
    protected void broadCastRecieved(Context context, Intent intent)
    {
        if(intent.getAction().equals(BroadCastConstant.LOCATION_REFRESH_CONSTANT))
        {
            ConsoleLog.i(TAG, "home Presenter location change broadcast recieved");
            LatLng latLng = DropMeLocatioListener.getLatLng(context);
            if(latLng != null)
            {
                listenToMap(latLng);
                if (mapChange != null) {
                    mapChange.moveMap(latLng);
                }
            }
        }
    }

    public interface MapChange
    {
        void addMarker(MarkerInfo markerInfo);

        void moveMarker(MarkerInfo markerInfo);

        void removeMarker(MarkerInfo markerInfo);

        void moveMap(LatLng latLng);
    }
}
