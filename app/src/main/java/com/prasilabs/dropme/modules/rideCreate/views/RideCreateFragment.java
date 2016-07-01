package com.prasilabs.dropme.modules.rideCreate.views;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.api.client.util.DateTime;
import com.prasilabs.constants.TempConstant;
import com.prasilabs.dropme.R;
import com.prasilabs.dropme.backend.dropMeApi.model.GeoPt;
import com.prasilabs.dropme.backend.dropMeApi.model.RideInput;
import com.prasilabs.dropme.core.CoreApp;
import com.prasilabs.dropme.core.CoreFragment;
import com.prasilabs.dropme.customs.NoDefaultSpinner;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.managers.UserManager;
import com.prasilabs.dropme.modules.mobileVerification.views.MobileVerificationFragment;
import com.prasilabs.dropme.modules.rideCreate.presenter.RideCreatePresenter;
import com.prasilabs.dropme.services.location.DropMeLocatioListener;
import com.prasilabs.dropme.utils.LocationUtils;
import com.prasilabs.dropme.utils.ViewUtil;
import com.prasilabs.util.DataUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by prasi on 14/6/16.
 */
public class RideCreateFragment extends CoreFragment<RideCreatePresenter> implements RideCreatePresenter.RideCreatePresenterCallBack
{

    private static final String TAG = RideCreateFragment.class.getSimpleName();
    private static final int PLACE_PICKER_REQUEST = 1;
    @BindView(R.id.select_vehicle)
    NoDefaultSpinner selectVehicleSpinner;
    @BindView(R.id.select_fare_rate)
    NoDefaultSpinner fareRateSpinner;
    @BindView(R.id.top_layout)
    LinearLayout topLayout;
    @BindView(R.id.vehicle_text)
    TextView vehicleTypeText;
    @BindView(R.id.fare_rate_text)
    TextView fareRateText;


    private long vehicleID = 0;
    private GeoPt destLoc;
    private Date startTime;
    private String destLocationName;
    private int farePerKm = 0;

    public static RideCreateFragment getInstance()
    {
        return new RideCreateFragment();
    }

    @Override
    protected RideCreatePresenter setCorePresenter() {
        return new RideCreatePresenter(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if(getFragmentView() == null)
        {
            setFragmentView(inflater.inflate(R.layout.fragment_create_ride, container, false));


            PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getCoreActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
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
            final List<String> stringList = new ArrayList<>();
            final ArrayAdapter<Integer> rateAdapter = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, rateList);
            stringList.add("CAR");
            stringList.add("BIKE");

            selectVehicleSpinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, stringList));

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

                    vehicleTypeText.setText(stringList.get(position));
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
                    fareRateText.setText(rateList.get(position) + " Rs");

                    farePerKm = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {

                }
            });

            fareRateText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fareRateSpinner.performClick();
                }
            });

            vehicleTypeText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectVehicleSpinner.performClick();
                }
            });
        }

        return getFragmentView();
    }

    @OnClick(R.id.ride_btn)
    protected void onRideClicked()
    {
        if(validateRide())
        {
            boolean isPhoneConfirmed = UserManager.isMobileVerified(getContext());
            if (isPhoneConfirmed) {
                createAndMakeApiCall(null);
            } else {
                MobileVerificationFragment.getInstance(this, false);
            }
        }
    }

    @OnClick(R.id.later_btn)
    protected void onLaterClicked()
    {
        if(validateRide())
        {
            boolean isPhoneConfirmed = UserManager.isMobileVerified(getContext());
            if (isPhoneConfirmed) {
                final Calendar calendar = Calendar.getInstance();
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        DateTime startDate = new DateTime(calendar.getTime());

                        createAndMakeApiCall(startDate);

                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            } else {
                MobileVerificationFragment.getInstance(this, false);
            }
        }
    }

    private void createAndMakeApiCall(DateTime startDate)
    {
        GeoPt source = LocationUtils.convertToGeoPt(DropMeLocatioListener.getLatLng(getContext()));

        RideInput rideInput = new RideInput();
        rideInput.setDestLoc(destLoc);
        rideInput.setDestLocName(destLocationName);
        rideInput.setSourceLoc(source);
        rideInput.setCurrentLoc(source);
        rideInput.setFarePerKm(farePerKm);
        rideInput.setVehicleId(vehicleID);
        rideInput.setDeviceId(CoreApp.getDeviceId());
        rideInput.setUserId(UserManager.getDropMeUser(getContext()).getId());
        rideInput.setStartDate(startDate);

        ViewUtil.showProgressView(getContext(), topLayout, true);
        getPresenter().createRide(rideInput);
    }


    private boolean validateRide()
    {
        boolean isValid = true;

        if(vehicleID == 0)
        {
            ViewUtil.t(getContext(), "Please select vehicle type");
            isValid = false;
        }
        else if(destLoc == null)
        {
            ViewUtil.t(getContext(), "Please select destination from dropdown");
            isValid = false;
        }
        else if(DataUtil.isEmpty(destLocationName))
        {
            ViewUtil.t(getContext(), "Please select destination from dropdown");
            isValid = false;
        }

        return isValid;
    }


    @Override
    public void rideCreated(RideInput rideInput)
    {
        ViewUtil.hideProgressView(getContext(), topLayout);
        ViewUtil.t(getContext(), "Your ride is created...");
        getCoreActivity().finish();
    }

    @Override
    public void rideCreateFailed()
    {
        ViewUtil.hideProgressView(getContext(), topLayout);
        ViewUtil.t(getContext(), "unabel to create Ride");
        ConsoleLog.i(TAG, "unabel to create Ride");
    }

    /*@OnClick(R.id.place_picker_btn)
    protected void onPlaceButtonClicked()
    {
        try
        {
            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(getCoreActivity());
            startActivityForResult(intent, PLACE_PICKER_REQUEST);

        }
        catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
           ConsoleLog.e(e);
        }
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ConsoleLog.i(TAG, "fragment on activity result came");
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {
            Place place = PlacePicker.getPlace(getContext(), data);
            onPlaceResult(place);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void onPlaceResult(Place place) {
        destLoc = LocationUtils.convertToGeoPt(place.getLatLng());
        destLocationName = String.valueOf(place.getName());

        //placePickerBtn.setText(destLocationName);
    }


}
