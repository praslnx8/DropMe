package com.prasilabs.dropme.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.prasilabs.dropme.R;
import com.prasilabs.dropme.backend.dropMeApi.model.GeoPt;
import com.prasilabs.dropme.backend.dropMeApi.model.VRide;
import com.prasilabs.dropme.core.CoreActivity;
import com.prasilabs.dropme.core.CoreApp;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.services.location.DropMeLocatioListener;
import com.prasilabs.dropme.utils.LocationUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RideCreateActivity extends CoreActivity
{
    private static final String TAG = RideCreateActivity.class.getSimpleName();

    public static void startRideCreateActivity(Context context)
    {
        Intent intent = new Intent(context, RideCreateActivity.class);
        context.startActivity(intent);
    }

    private long vehicleID = 0;
    private GeoPt destLoc;
    private Date startTime;

    @BindView(R.id.select_vehicle)
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);

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

        List<String> stringList = new ArrayList<>();
        stringList.add("CAR");
        stringList.add("BIKE");
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, stringList));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                ConsoleLog.i(TAG, "selcted is :" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.ride_btn)
    protected void onRideClicked()
    {
        if(validateRide())
        {
            GeoPt source = LocationUtils.convertToGeoPt(DropMeLocatioListener.getLatLng(this));

            VRide vRide = new VRide();
            vRide.setSourceLoc(source);
            vRide.setDestLoc(destLoc);
            vRide.setDeviceId(CoreApp.getDeviceId());

        }
    }

    @OnClick(R.id.later_btn)
    protected void onLaterClicked()
    {
        //TODO ask time
    }


    private boolean validateRide()
    {
        boolean isValid = false;


        return isValid;
    }


}
