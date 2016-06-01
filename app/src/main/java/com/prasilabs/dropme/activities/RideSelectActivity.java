package com.prasilabs.dropme.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.prasilabs.dropme.R;
import com.prasilabs.dropme.backend.dropMeApi.model.RideDetail;
import com.prasilabs.dropme.core.CoreActivity;
import com.prasilabs.dropme.customs.MyRecyclerView;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.modules.rideSelect.presenters.RideSelectPresenter;
import com.prasilabs.dropme.modules.rideSelect.views.RideSelectAdapter;

import java.util.List;

import butterknife.BindView;

public class RideSelectActivity extends CoreActivity<RideSelectPresenter> implements RideSelectPresenter.GetRidesCallBack
{
    private static final String TAG = RideSelectActivity.class.getSimpleName();
    RideSelectPresenter rideSelectPresenter = new RideSelectPresenter();

    @BindView(R.id.container)
    LinearLayout mainLayout;
    @BindView(R.id.rider_list)
    MyRecyclerView ridersListView;

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

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("Enter Destination");
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place)
            {
                ConsoleLog.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status)
            {
                ConsoleLog.i(TAG, "An error occurred: " + status);
            }
        });

        rideSelectAdapter = RideSelectAdapter.getInstance(this);
        ridersListView.setAdapter(rideSelectAdapter);

        rideSelectPresenter.getRideDetailList(this);
    }

    @Override
    protected RideSelectPresenter setCorePresenter()
    {
        return rideSelectPresenter;
    }

    @Override
    public void getRides(List<RideDetail> rideDetailList)
    {
        rideSelectAdapter.addListItem(rideDetailList);
    }
}
