package com.prasilabs.dropme.modules.home.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.prasilabs.dropme.R;
import com.prasilabs.dropme.core.CoreFragment;
import com.prasilabs.dropme.customs.MapLoader;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.modules.home.presenters.HomePresenter;
import com.prasilabs.dropme.pojo.MarkerInfo;
import com.prasilabs.dropme.services.location.DropMeGLocationService;
import com.prasilabs.dropme.utils.MarkerUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by prasi on 27/5/16.
 */
public class HomeFragment extends CoreFragment<HomePresenter> implements HomePresenter.MapChange
{
    private static final String TAG = HomeFragment.class.getSimpleName();
    private HomePresenter homePresenter = HomePresenter.newInstance(this);
    private static HomeFragment homeFragment;
    private MapLoader mapLoader;

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
                }
            });

            DropMeGLocationService.start(getContext());
        }

        return getFragmentView();
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
    }

    @OnClick(R.id.book_ride_btn)
    protected void bookRide()
    {
        ConsoleLog.i(TAG, "book ride clicked");
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
        mapView.onDestroy();
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
}
