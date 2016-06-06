package com.prasilabs.dropme.modules.home.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.prasilabs.dropme.R;
import com.prasilabs.dropme.activities.RideCreateActivity;
import com.prasilabs.dropme.activities.RideSelectActivity;
import com.prasilabs.dropme.backend.dropMeApi.model.RideInput;
import com.prasilabs.dropme.core.CoreFragment;
import com.prasilabs.dropme.customs.MapBoxMapLoader;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.modules.home.presenters.HomePresenter;
import com.prasilabs.dropme.pojo.MarkerInfo;
import com.prasilabs.dropme.services.location.DropMeGLocationService;
import com.prasilabs.dropme.services.location.DropMeLocatioListener;
import com.prasilabs.dropme.utils.LocationUtils;
import com.prasilabs.dropme.utils.MarkerUtil;
import com.prasilabs.dropme.utils.ViewUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by prasi on 27/5/16.
 */
public class HomeFragment extends CoreFragment<HomePresenter> implements HomePresenter.MapChange, HomePresenter.CancelRideCallBack
{
    private static final String TAG = HomeFragment.class.getSimpleName();
    private HomePresenter homePresenter = HomePresenter.newInstance(this);
    private static HomeFragment homeFragment;
    private MapBoxMapLoader mapLoader;
    private boolean isProgress;

    public static HomeFragment getHomeFragment()
    {
        if(homeFragment == null)
        {
            homeFragment = new HomeFragment();
        }

        return homeFragment;
    }

    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.top_layout)
    FrameLayout topLayout;
    @BindView(R.id.home_button_layout)
    LinearLayout homeButtonLayout;
    @BindView(R.id.cancel_button_layout)
    LinearLayout cancelButtonLayout;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        MapsInitializer.initialize(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if(getFragmentView() == null)
        {
            setFragmentView(inflater.inflate(R.layout.fragment_home, container, false));

            mapLoader = new MapBoxMapLoader(mapView,savedInstanceState);

            homeButtonLayout.setVisibility(View.GONE);
            mapLoader.loadMap(new MapBoxMapLoader.MapLoaderCallBack() {
                @Override
                public void mapLoaded()
                {
                    ConsoleLog.i(TAG, "map loaded");

                    DropMeGLocationService.start(getContext());

                    isProgress = true;
                    ViewUtil.showProgressView(getContext(), topLayout, true);
                    homePresenter.getCurrentRide();
                }
            });
        }

        return getFragmentView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected HomePresenter setCorePresenter()
    {
        return homePresenter;
    }

    @OnClick(R.id.offer_ride_btn)
    protected void offerRide()
    {
        ConsoleLog.i(TAG, "offer ride clicked");
        RideCreateActivity.startRideCreateActivity(getContext());
    }

    @OnClick(R.id.cancel_btn)
    protected void cancelClicked()
    {
        isProgress = true;
        ViewUtil.showProgressView(getContext(), topLayout, true);
        ConsoleLog.i(TAG, "cancel clicked");
        homePresenter.cancelRide(this);
    }

    @OnClick(R.id.book_ride_btn)
    protected void bookRide()
    {
        ConsoleLog.i(TAG, "book ride clicked");
        RideSelectActivity.startRideSelectActivity(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume()
    {
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
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        homeFragment = null;
    }

    @Override
    public void addMarker(MarkerInfo markerInfo)
    {
        mapLoader.addMarker(markerInfo.getKey(), markerInfo.getLoc(), MarkerUtil.getMarkerResId(markerInfo));
    }

    @Override
    public void moveMarker(MarkerInfo markerInfo)
    {
        mapLoader.addMarker(markerInfo.getKey(), markerInfo.getLoc(), MarkerUtil.getMarkerResId(markerInfo));
    }

    @Override
    public void removeMarker(MarkerInfo markerInfo)
    {
        mapLoader.removeMarker(markerInfo.getKey());
    }

    @Override
    public void addSourceAndDestination(RideInput rideInput)
    {
        ConsoleLog.i(TAG, "add source and destination called");
        if(isProgress)
        {
            ViewUtil.hideProgressView(getContext(), topLayout);
            isProgress = false;
        }
        if(mapLoader != null)
        {
            if(rideInput != null)
            {
                LatLng currentLatlng = DropMeLocatioListener.getLatLng(getContext());
                LatLng destLatLng = LocationUtils.convertToLatLng(rideInput.getDestLoc());

                mapLoader.removeMarker(MarkerUtil.SOURCE_MARKER_KEY);
                mapLoader.removeMarker(MarkerUtil.DEST_MARKER_KEY);
                mapLoader.addMarker(MarkerUtil.SOURCE_MARKER_KEY, currentLatlng, MarkerUtil.SOURCE_MARKER);
                mapLoader.addMarker(MarkerUtil.DEST_MARKER_KEY, destLatLng, MarkerUtil.DEST_MARKER);
                mapLoader.showDirection(currentLatlng, destLatLng, true);

                homeButtonLayout.setVisibility(View.GONE);
                cancelButtonLayout.setVisibility(View.VISIBLE);

                mapLoader.moveToLoc(currentLatlng);
            }
            else
            {
                mapLoader.removeMarker(MarkerUtil.SOURCE_MARKER_KEY);
                mapLoader.removeMarker(MarkerUtil.DEST_MARKER_KEY);
                mapLoader.clearPolyLine();

                homeButtonLayout.setVisibility(View.VISIBLE);
                cancelButtonLayout.setVisibility(View.GONE);

                LatLng currentLatlng = DropMeLocatioListener.getLatLng(getContext());
                mapLoader.moveToLoc(currentLatlng);

            }

        }
    }

    @Override
    public void rideCanceled()
    {
        if(isProgress)
        {
            ViewUtil.hideProgressView(getContext(), topLayout);
            isProgress = false;
        }
        if(mapLoader != null)
        {
            mapLoader.removeMarker(MarkerUtil.SOURCE_MARKER_KEY);
            mapLoader.removeMarker(MarkerUtil.DEST_MARKER_KEY);
            mapLoader.clearPolyLine();

            homeButtonLayout.setVisibility(View.VISIBLE);
            cancelButtonLayout.setVisibility(View.GONE);

            LatLng currentLatlng = DropMeLocatioListener.getLatLng(getContext());
            mapLoader.moveToLoc(currentLatlng);
        }
    }

    @Override
    public void rideCancelFailed(String message)
    {
        if(isProgress)
        {
            ViewUtil.hideProgressView(getContext(), topLayout);
            isProgress = false;
        }
        ViewUtil.t(getContext(), "Unable to cancel the ride");
    }
}
