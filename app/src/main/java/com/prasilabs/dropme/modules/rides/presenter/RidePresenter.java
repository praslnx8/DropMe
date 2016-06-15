package com.prasilabs.dropme.modules.rides.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.prasilabs.dropme.backend.dropMeApi.model.RideInput;
import com.prasilabs.dropme.constants.BroadCastConstant;
import com.prasilabs.dropme.core.CorePresenter;
import com.prasilabs.dropme.managers.RideManager;
import com.prasilabs.dropme.modelengines.HomeGeoModelEngine;
import com.prasilabs.dropme.modelengines.RideModelEngine;
import com.prasilabs.dropme.pojo.MarkerInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by prasi on 14/6/16.
 */
public class RidePresenter extends CorePresenter {
    private GetGeoCallBack getGeoCallBack;
    private GetRideCallBack getRideCallBack;

    @Override
    protected void onCreateCalled() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadCastConstant.GEO_REFRESH_INTENT);
        intentFilter.addAction(BroadCastConstant.RIDE_REFRESH_INTENT);
        registerReciever(intentFilter);
    }

    public void getCurrentRide(final GetRideCallBack getRideCallBack) {
        this.getRideCallBack = getRideCallBack;
        RideModelEngine.getInstance().getCurrentRide(new RideModelEngine.GetCurrentRideCallBack() {
            @Override
            public void getCurrentRide(RideInput rideInput) {
                if (getRideCallBack != null) {
                    getRideCallBack.getRide(rideInput);
                }
            }
        });
    }

    public void getGeoList(GetGeoCallBack getGeoCallBack) {
        this.getGeoCallBack = getGeoCallBack;

        handleGeoChange();
    }

    private void handleGeoChange() {
        List<MarkerInfo> markerInfoList = new ArrayList<>();
        Map<String, MarkerInfo> geoMarkerMap = HomeGeoModelEngine.getInstance().getGeoMarkerMap();

        for (Map.Entry<String, MarkerInfo> entry : geoMarkerMap.entrySet()) {
            markerInfoList.add(entry.getValue());
        }

        if (getGeoCallBack != null) {
            getGeoCallBack.getGeoList(markerInfoList);
        }
    }

    @Override
    protected void broadCastRecieved(Context context, Intent intent) {
        if (intent.getAction().equals(BroadCastConstant.GEO_REFRESH_INTENT)) {
            handleGeoChange();
        } else if (intent.getAction().equals(BroadCastConstant.RIDE_REFRESH_INTENT)) {
            RideInput rideInput = RideManager.getRideLite(context);
            if (getRideCallBack != null) {
                getRideCallBack.getRide(rideInput);
            }
        }
    }

    public void cancelRide(final CancelRideCallBack cancelRideCallBack) {
        RideModelEngine.getInstance().cancelRide(new RideModelEngine.CancleRideCallBack() {
            @Override
            public void cancel(boolean isSuccess) {
                if (cancelRideCallBack != null) {
                    if (isSuccess) {
                        cancelRideCallBack.rideCanceled();
                    } else {
                        cancelRideCallBack.rideCancelFailed();
                    }
                }
            }
        });
    }

    public interface GetRideCallBack {
        void getRide(RideInput rideInput);
    }

    public interface GetGeoCallBack {
        void getGeoList(List<MarkerInfo> markerInfoList);
    }

    public interface CancelRideCallBack {
        void rideCanceled();

        void rideCancelFailed();
    }
}
