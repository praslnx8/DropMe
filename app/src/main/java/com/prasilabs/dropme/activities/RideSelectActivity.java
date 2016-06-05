package com.prasilabs.dropme.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.prasilabs.dropme.R;
import com.prasilabs.dropme.backend.dropMeApi.model.RideDetail;
import com.prasilabs.dropme.core.CoreActivity;
import com.prasilabs.dropme.customs.FlowLayout;
import com.prasilabs.dropme.customs.MyRecyclerView;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.modules.rideSelect.presenters.RideFilter;
import com.prasilabs.dropme.modules.rideSelect.presenters.RideSelectPresenter;
import com.prasilabs.dropme.modules.rideSelect.views.RideFilterView;
import com.prasilabs.dropme.modules.rideSelect.views.RideSelectAdapter;
import com.prasilabs.dropme.utils.DialogUtils;
import com.prasilabs.dropme.utils.ViewUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RideSelectActivity extends CoreActivity<RideSelectPresenter> implements RideSelectPresenter.GetRidesCallBack, RideSelectPresenter.RenderViewCallBack
{
    private static final String TAG = RideSelectActivity.class.getSimpleName();
    RideSelectPresenter rideSelectPresenter = new RideSelectPresenter(this);

    @BindView(R.id.top_layout)
    LinearLayout topLayout;
    @BindView(R.id.rider_list)
    MyRecyclerView ridersListView;
    @BindView(R.id.empty_layout)
    LinearLayout emptyJokerLayout;
    @BindView(R.id.flow_layout)
    FlowLayout filterShowLayout;

    private RideSelectAdapter rideSelectAdapter;

    public static void startRideSelectActivity(Context context)
    {
        Intent intent = new Intent(context, RideSelectActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_select);

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().hide();
        }

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("Search Destination");
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place)
            {
                ConsoleLog.i(TAG, "Place: " + place.getName());

                ViewUtil.showProgressView(RideSelectActivity.this, topLayout, true);
                rideSelectPresenter.filterByDestination(String.valueOf(place.getName()), place.getLatLng(), RideSelectActivity.this);
            }

            @Override
            public void onError(Status status)
            {
                ConsoleLog.i(TAG, "An error occurred: " + status);
            }
        });

        rideSelectAdapter = RideSelectAdapter.getInstance(this);
        ridersListView.setAdapter(rideSelectAdapter);

        ViewUtil.showProgressView(this, topLayout, true);

        rideSelectPresenter.getRideDetailList(this);
    }

    @OnClick(R.id.filter_btn)
    protected void onFilterClicked()
    {
        DialogUtils.showRideSelectFilter(this, new DialogUtils.FilterDialogCallBack() {
            @Override
            public void filterApplied()
            {
                ViewUtil.showProgressView(RideSelectActivity.this, topLayout, true);
                rideSelectPresenter.filter(RideSelectActivity.this);
            }
        });
    }

    @Override
    protected RideSelectPresenter setCorePresenter()
    {
        return rideSelectPresenter;
    }

    @Override
    public void getRides(List<RideDetail> rideDetailList)
    {
        ConsoleLog.i(TAG, "ridelist came for the views");
        ViewUtil.hideProgressView(this, topLayout);
        if(rideDetailList != null && rideDetailList.size() > 0)
        {
            rideSelectAdapter.clearAndAddItem(rideDetailList);
            ridersListView.setVisibility(View.VISIBLE);
            emptyJokerLayout.setVisibility(View.GONE);
        }
        else
        {
            emptyJokerLayout.setVisibility(View.VISIBLE);
            ridersListView.setVisibility(View.GONE);
        }
    }

    @Override
    public void renderFilterData()
    {
        filterShowLayout.removeAllViews();

        RideFilter rideFilter = RideFilter.getInstance();

        List<View> rideFilterView = rideFilter.getRideFilterViews(this, new RideFilterView.FilterCancelCallBack()
        {
            @Override
            public void filterDeleted()
            {
                ViewUtil.showProgressView(RideSelectActivity.this, topLayout, true);
                rideSelectPresenter.filter(RideSelectActivity.this);
            }
        });

        for(View view : rideFilterView)
        {
            filterShowLayout.addView(view);
        }

    }
}
