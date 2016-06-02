package com.prasilabs.dropme.modules.home.presenters;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.google.android.gms.maps.model.LatLng;
import com.prasilabs.dropme.backend.dropMeApi.model.RideInput;
import com.prasilabs.dropme.constants.BroadCastConstant;
import com.prasilabs.dropme.core.CorePresenter;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.managers.RideManager;
import com.prasilabs.dropme.modelengines.HomeGeoModelEngine;
import com.prasilabs.dropme.modelengines.RideModelEngine;
import com.prasilabs.dropme.pojo.MarkerInfo;
import com.prasilabs.dropme.services.location.DropMeLocatioListener;

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

    private HomePresenter(MapChange mapChange)
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

    public void getCurrentRide()
    {
        RideModelEngine.getInstance().getCurrentRide();
    }

    public void cancelRide(final CancelRideCallBack cancelRideCallBack)
    {
        RideModelEngine.getInstance().cancelRide(new RideModelEngine.CancleRideCallBack() {
            @Override
            public void cancel(boolean isSuccess)
            {
                if(cancelRideCallBack != null)
                {
                    if (isSuccess)
                    {
                        cancelRideCallBack.rideCanceled();
                    }
                    else
                    {
                        cancelRideCallBack.rideCancelFailed("Unable to cancel the current rides");
                    }
                }
            }
        });
    }

    @Override
    protected void onCreateCalled()
    {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadCastConstant.LOCATION_REFRESH_CONSTANT);
        intentFilter.addAction(BroadCastConstant.RIDE_REFRESH_INTENT);
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
            }
            RideInput rideInput = RideManager.getRideLite(context);

            if(mapChange != null)
            {
                mapChange.addSourceAndDestination(rideInput);
            }
        }
        else if(intent.getAction().equals(BroadCastConstant.RIDE_REFRESH_INTENT))
        {
            RideInput rideInput = RideManager.getRideLite(context);

            if(mapChange != null)
            {
                mapChange.addSourceAndDestination(rideInput);
            }
        }
    }

    public interface CancelRideCallBack
    {
        void rideCanceled();

        void rideCancelFailed(String message);
    }

    public interface MapChange
    {
        void addMarker(MarkerInfo markerInfo);

        void moveMarker(MarkerInfo markerInfo);

        void removeMarker(MarkerInfo markerInfo);

        void addSourceAndDestination(RideInput rideInput);
    }
}
