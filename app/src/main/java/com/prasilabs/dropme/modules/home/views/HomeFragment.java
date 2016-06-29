package com.prasilabs.dropme.modules.home.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.prasilabs.dropme.R;
import com.prasilabs.dropme.activities.GenericActivity;
import com.prasilabs.dropme.core.CoreFragment;
import com.prasilabs.dropme.customs.MapLoader;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.modules.home.presenters.HomePresenter;
import com.prasilabs.dropme.pojo.MarkerInfo;
import com.prasilabs.dropme.services.location.DropMeGLocationService;
import com.prasilabs.dropme.services.location.DropMeLocatioListener;
import com.prasilabs.dropme.utils.MarkerUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by prasi on 27/5/16.
 */
public class HomeFragment extends CoreFragment<HomePresenter> implements HomePresenter.MapChange
{
    private static final String TAG = HomeFragment.class.getSimpleName();
    private static HomeFragment instance;
    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.top_layout)
    FrameLayout topLayout;
    private MapLoader mapLoader;

    public static HomeFragment getInstance() {
        if (instance == null) {
            instance = new HomeFragment();
        }

        return instance;
    }

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

            mapLoader = new MapLoader(mapView,savedInstanceState);

            mapLoader.loadMap(new MapLoader.MapLoaderCallBack() {
                @Override
                public void mapLoaded()
                {
                    ConsoleLog.i(TAG, "map loaded");
                    LatLng latLng = DropMeLocatioListener.getLatLng(getContext());
                    mapLoader.moveToLoc(latLng);
                    DropMeGLocationService.start(getContext());
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
        return new HomePresenter(this);
    }

    @OnClick(R.id.offer_ride_btn)
    protected void offerRide()
    {
        ConsoleLog.i(TAG, "offer ride clicked");
        GenericActivity.openRideCreate(getContext());
    }

    @OnClick(R.id.book_ride_btn)
    protected void bookRide()
    {
        ConsoleLog.i(TAG, "book ride clicked");
        GenericActivity.openRideSelect(getContext());
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
    public void onDestroyView()
    {
        mapView.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    @Override
    public void addMarker(MarkerInfo markerInfo)
    {
        mapLoader.addMarker(markerInfo.getKey(), markerInfo.getLoc(), false, MarkerUtil.getMarkerResId(markerInfo), markerInfo.getMarkerDirection());
    }

    @Override
    public void moveMarker(MarkerInfo markerInfo)
    {
        mapLoader.addMarker(markerInfo.getKey(), markerInfo.getLoc(), false, MarkerUtil.getMarkerResId(markerInfo), markerInfo.getMarkerDirection());
    }

    @Override
    public void removeMarker(MarkerInfo markerInfo)
    {
        mapLoader.removeMarker(markerInfo.getKey());
    }

    @Override
    public void moveMap(LatLng latLng)
    {
        mapLoader.moveToLoc(latLng);
        mapLoader.removeMarker("me");
        mapLoader.addMarker("me", latLng, R.drawable.ic_location_pointer);
    }
}
