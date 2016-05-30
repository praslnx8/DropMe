package com.prasilabs.dropme.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.prasilabs.dropme.R;
import com.prasilabs.dropme.core.CoreActivity;
import com.prasilabs.dropme.debug.ConsoleLog;

public class RideCreateActivity extends CoreActivity
{
    private static final String TAG = RideCreateActivity.class.getSimpleName();

    public static void startRideCreateActivity(Context context)
    {
        Intent intent = new Intent(context, RideCreateActivity.class);
        context.startActivity(intent);
    }

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

    }
}
