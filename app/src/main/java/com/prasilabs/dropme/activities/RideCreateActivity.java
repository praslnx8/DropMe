package com.prasilabs.dropme.activities;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.api.client.util.DateTime;
import com.prasilabs.constants.TempConstant;
import com.prasilabs.dropme.R;
import com.prasilabs.dropme.backend.dropMeApi.model.GeoPt;
import com.prasilabs.dropme.backend.dropMeApi.model.RideInput;
import com.prasilabs.dropme.backend.dropMeApi.model.VDropMeUser;
import com.prasilabs.dropme.core.CoreActivity;
import com.prasilabs.dropme.core.CoreApp;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.managers.UserManager;
import com.prasilabs.dropme.modules.rideCreate.presenter.RideCreatePresenter;
import com.prasilabs.dropme.services.location.DropMeLocatioListener;
import com.prasilabs.dropme.utils.LocationUtils;
import com.prasilabs.dropme.utils.ViewUtil;
import com.prasilabs.util.DataUtil;
import com.prasilabs.util.ValidateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RideCreateActivity extends CoreActivity<RideCreatePresenter> implements RideCreatePresenter.RideCreatePresenterCallBack
{
    private static final String TAG = RideCreateActivity.class.getSimpleName();

    private RideCreatePresenter rideCreatePresenter = RideCreatePresenter.newInstance(this);

    public static void startRideCreateActivity(Context context)
    {
        Intent intent = new Intent(context, RideCreateActivity.class);
        context.startActivity(intent);
    }

    private long vehicleID = 0;
    private GeoPt destLoc;
    private Date startTime;
    private String destLocationName;
    private int farePerKm = 0;

    @BindView(R.id.select_vehicle)
    Spinner selectVehicleSpinner;
    @BindView(R.id.select_fare_rate)
    Spinner fareRateSpinner;
    @BindView(R.id.top_layout)
    LinearLayout topLayout;
    @BindView(R.id.phone_text)
    EditText phoneText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().hide();
        }

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("Enter Destination");
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place)
            {
                ConsoleLog.i(TAG, "Place: " + place.getName());
                destLocationName = String.valueOf(place.getName());
                destLoc = LocationUtils.convertToGeoPt(place.getLatLng());
            }

            @Override
            public void onError(Status status)
            {
                ConsoleLog.i(TAG, "An error occurred: " + status);
            }
        });

        final List<Integer> rateList = new ArrayList<>();
        List<String> stringList = new ArrayList<>();
        final ArrayAdapter<Integer> rateAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, android.R.id.text1, rateList);
        stringList.add("CAR");
        stringList.add("BIKE");
        selectVehicleSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, stringList));

        selectVehicleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(position == 0)
                {
                    vehicleID = TempConstant.VCAR;
                    for(int i=0;i<=20;i++)
                    {
                        if(i == 0)
                        {
                            rateList.clear();
                        }
                        rateList.add(i);
                    }
                    rateAdapter.notifyDataSetChanged();
                }
                else
                {
                    vehicleID = TempConstant.VBIKE;

                    for(int i=0;i<=10;i++)
                    {
                        if(i == 0)
                        {
                            rateList.clear();
                        }
                        rateList.add(i);
                    }
                    rateAdapter.notifyDataSetChanged();
                }
                ConsoleLog.i(TAG, "selcted is :" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        for(int i=0;i<=20;i++)
        {
            if(i == 0)
            {
                rateList.clear();
            }
            rateList.add(i);
        }
        fareRateSpinner.setAdapter(rateAdapter);

        fareRateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                ConsoleLog.i(TAG, "selcted is :" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        VDropMeUser vDropMeUser = UserManager.getDropMeUser(this);
        if(vDropMeUser != null && !DataUtil.isEmpty(vDropMeUser.getMobile()))
        {
            phoneText.setText(vDropMeUser.getMobile());
        }

    }

    @Override
    protected RideCreatePresenter setCorePresenter() {
        return rideCreatePresenter;
    }

    @OnClick(R.id.ride_btn)
    protected void onRideClicked()
    {
        if(validateRide())
        {
            createAndMakeApiCall(null);
        }
    }

    @OnClick(R.id.later_btn)
    protected void onLaterClicked()
    {
        if(validateRide())
        {
            final Calendar calendar = Calendar.getInstance();
            new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                {

                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    DateTime startDate = new DateTime(calendar.getTime());

                    createAndMakeApiCall(startDate);

                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
        }
    }

    private void createAndMakeApiCall(DateTime startDate)
    {
        GeoPt source = LocationUtils.convertToGeoPt(DropMeLocatioListener.getLatLng(RideCreateActivity.this));

        String phoneNo = phoneText.getText().toString();
        UserManager.savePhoneNo(this, phoneNo);

        RideInput rideInput = new RideInput();
        rideInput.setDestLoc(destLoc);
        rideInput.setDestLocName(destLocationName);
        rideInput.setSourceLoc(source);
        rideInput.setCurrentLoc(source);
        rideInput.setFarePerKm(farePerKm);
        rideInput.setVehicleId(vehicleID);
        rideInput.setDeviceId(CoreApp.getDeviceId());
        rideInput.setUserId(UserManager.getDropMeUser(RideCreateActivity.this).getId());
        rideInput.setStartDate(startDate);
        rideInput.setPhoneNo(phoneNo);

        ViewUtil.showProgressView(this, topLayout, true);
        rideCreatePresenter.createRide(rideInput);
    }


    private boolean validateRide()
    {
        boolean isValid = true;

        if(vehicleID == 0)
        {
            ViewUtil.t(this, "Please select vehicle type");
            isValid = false;
        }
        else if(destLoc == null)
        {
            ViewUtil.t(this, "Please select destination from dropdown");
            isValid = false;
        }
        else if(DataUtil.isEmpty(destLocationName))
        {
            ViewUtil.t(this, "Please select destination from dropdown");
            isValid = false;
        }
        else if(!ValidateUtil.validateMobile(phoneText.getText().toString()))
        {
            if(DataUtil.isEmpty(phoneText.getText().toString()))
            {
                phoneText.setError("Required");
                ViewUtil.t(this, "Please add phone number");
            }
            else
            {
                phoneText.setError("Add proper number");
                ViewUtil.t(this, "Please add proper phone number");
            }
            isValid = false;
        }

        return isValid;
    }


    @Override
    public void rideCreated()
    {
        ViewUtil.hideProgressView(this, topLayout);
        ViewUtil.t(this, "Your ride is created...");
        finish();
    }

    @Override
    public void rideCreateFailed()
    {
        ViewUtil.hideProgressView(this, topLayout);
        ViewUtil.t(this, "unabel to create Ride");
        ConsoleLog.i(TAG, "unabel to create Ride");
    }
}
