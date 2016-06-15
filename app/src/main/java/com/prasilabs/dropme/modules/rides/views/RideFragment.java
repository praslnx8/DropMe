package com.prasilabs.dropme.modules.rides.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.prasilabs.dropme.R;
import com.prasilabs.dropme.backend.dropMeApi.model.RideInput;
import com.prasilabs.dropme.core.CoreFragment;
import com.prasilabs.dropme.customs.MapLoader;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.modules.rides.presenter.RidePresenter;
import com.prasilabs.dropme.pojo.MarkerInfo;
import com.prasilabs.dropme.utils.LocationUtils;
import com.prasilabs.dropme.utils.MarkerUtil;
import com.prasilabs.dropme.utils.ViewUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by prasi on 14/6/16.
 */
public class RideFragment extends CoreFragment<RidePresenter> implements RidePresenter.GetGeoCallBack, RidePresenter.CancelRideCallBack {
    private static final String TAG = RideFragment.class.getSimpleName();
    @BindView(R.id.map_view)
    MapView mapView;
    private RideInput rideInput;
    private RideCancelCallBack rideCancelCallBack;
    private MapLoader mapLoader;

    public static RideFragment newInstance(RideInput rideInput, RideCancelCallBack rideCancelCallBack) {
        RideFragment rideFragment = new RideFragment();
        rideFragment.rideInput = rideInput;
        rideFragment.rideCancelCallBack = rideCancelCallBack;

        return rideFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapsInitializer.initialize(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getFragmentView() == null) {
            setFragmentView(inflater.inflate(R.layout.fragment_ride, container, false));

            mapLoader = new MapLoader(mapView, savedInstanceState);
        }

        mapLoader.loadMap(new MapLoader.MapLoaderCallBack() {
            @Override
            public void mapLoaded() {
                mapLoader.showDirection(LocationUtils.convertToLatLng(rideInput.getSourceLoc()), LocationUtils.convertToLatLng(rideInput.getDestLoc()), true);

                getPresenter().getGeoList(RideFragment.this);
            }
        });


        return getFragmentView();
    }

    @Override
    protected RidePresenter setCorePresenter() {
        return new RidePresenter();
    }

    @OnClick(R.id.end_ride_btn)
    protected void endRideClicked() {
        ViewUtil.t(getContext(), "Long press to end the ride");
    }


    @OnLongClick(R.id.end_ride_btn)
    protected boolean endRideLongClicked() {
        getPresenter().cancelRide(this);

        return false;
    }

    @Override
    public void getGeoList(List<MarkerInfo> markerInfoList) {
        for (MarkerInfo markerInfo : markerInfoList) {
            mapLoader.addMarker(markerInfo.getKey(), markerInfo.getLoc(), MarkerUtil.getMarkerResId(markerInfo));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        mapView.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void rideCanceled() {
        ViewUtil.t(getContext(), "Ride is ended");
        if (rideCancelCallBack != null) {
            rideCancelCallBack.rideCanceled();
        } else {
            ConsoleLog.i(TAG, "rideCallBack is null");
        }
    }

    @Override
    public void rideCancelFailed() {
        ViewUtil.t(getContext(), "unable to end ride");
    }

    public interface RideCancelCallBack {
        void rideCanceled();
    }
}
