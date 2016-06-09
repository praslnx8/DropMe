package com.prasilabs.dropme.activities;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.api.client.util.DateTime;
import com.prasilabs.dropme.R;
import com.prasilabs.dropme.backend.dropMeApi.model.GeoPt;
import com.prasilabs.dropme.backend.dropMeApi.model.RideAlertIo;
import com.prasilabs.dropme.core.CoreActivity;
import com.prasilabs.dropme.customs.RangeTimePickerDialog;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.modules.rideAlerts.presenters.AlertCreatePresenter;
import com.prasilabs.dropme.utils.LocationUtils;
import com.prasilabs.dropme.utils.ViewUtil;
import com.prasilabs.enums.Gender;
import com.prasilabs.enums.VehicleType;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by prasi on 9/6/16.
 */
public class CreateAlertActivity extends CoreActivity<AlertCreatePresenter> implements AlertCreatePresenter.CreateAlertCallBack
{
    private static final String TAG = CreateAlertActivity.class.getSimpleName();
    private AlertCreatePresenter alertCreatePresenter = new AlertCreatePresenter();

    public static void openCreateAlertActivity(Context context)
    {
        Intent intent = new Intent(context, CreateAlertActivity.class);
        context.startActivity(intent);
    }

    private GeoPt sourceLatLng;
    private String sourceLocName;

    private GeoPt destLatLng;
    private String destLocName;

    private DateTime startTime;
    private DateTime endTime;

    @BindView(R.id.male_btn)
    CheckBox maleBtn;
    @BindView(R.id.female_btn)
    CheckBox femaleBtn;
    @BindView(R.id.car_btn)
    CheckBox carBtn;
    @BindView(R.id.bike_btn)
    CheckBox bikeBtn;

    @BindView(R.id.start_btn)
    Button startBtn;
    @BindView(R.id.end_btn)
    Button endBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alert_create);

        PlaceAutocompleteFragment sourceLocFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.source_place_fragment);
        sourceLocFragment.setHint("* Enter Source");
        sourceLocFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place)
            {
                ConsoleLog.i(TAG, "Place: " + place.getName());
                sourceLocName = String.valueOf(place.getName());
                sourceLatLng = LocationUtils.convertToGeoPt(place.getLatLng());
            }

            @Override
            public void onError(Status status)
            {
                ConsoleLog.i(TAG, "An error occurred: " + status);
            }
        });

        PlaceAutocompleteFragment destLocFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.dest_place_fragment);
        destLocFragment.setHint("* Enter Destination");
        destLocFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place)
            {
                ConsoleLog.i(TAG, "Place: " + place.getName());
                destLocName = String.valueOf(place.getName());
                destLatLng = LocationUtils.convertToGeoPt(place.getLatLng());
            }

            @Override
            public void onError(Status status)
            {
                ConsoleLog.i(TAG, "An error occurred: " + status);
            }
        });
    }

    @OnClick(R.id.start_btn)
    protected void onTimeStart()
    {
        final Calendar calendar = Calendar.getInstance();
        RangeTimePickerDialog rangeTimePickerDialog = new RangeTimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {

                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                startTime = new DateTime(calendar.getTime());

                startBtn.setText(hourOfDay + " : " + minute);

            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);

        if(endTime != null)
        {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTimeInMillis(endTime.getValue());
            rangeTimePickerDialog.setMax(calendar1.get(Calendar.HOUR_OF_DAY), calendar1.get(Calendar.MINUTE));
        }

        rangeTimePickerDialog.show();
    }

    @OnClick(R.id.end_btn)
    protected void onTimeEnd()
    {
        final Calendar calendar = Calendar.getInstance();
        RangeTimePickerDialog rangeTimePickerDialog = new RangeTimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {

                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                endTime = new DateTime(calendar.getTime());

                endBtn.setText(hourOfDay + " : " + minute);

            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);

        if(startTime != null)
        {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTimeInMillis(startTime.getValue());
            rangeTimePickerDialog.setMin(calendar1.get(Calendar.HOUR_OF_DAY), calendar1.get(Calendar.MINUTE));
        }

        rangeTimePickerDialog.show();
    }

    @OnClick(R.id.create_btn)
    protected void createAlert()
    {
        if(isValid())
        {
            RideAlertIo rideAlertIo = new RideAlertIo();
            rideAlertIo.setSource(sourceLatLng);
            rideAlertIo.setSourceName(sourceLocName);
            rideAlertIo.setDest(destLatLng);
            rideAlertIo.setDestName(destLocName);

            if(maleBtn.isChecked() && !femaleBtn.isChecked())
            {
                rideAlertIo.setGender(Gender.Male.name());
            }
            else if(!maleBtn.isChecked() && femaleBtn.isChecked())
            {
                rideAlertIo.setGender(Gender.Female.name());
            }

            if(carBtn.isChecked() && !bikeBtn.isChecked())
            {
                rideAlertIo.setVehicleType(VehicleType.Car.name());
            }
            else if(!carBtn.isChecked() && bikeBtn.isChecked())
            {
                rideAlertIo.setVehicleType(VehicleType.Bike.name());
            }

            if(startTime != null && endTime != null)
            {
                rideAlertIo.setStartTime(startTime);
                rideAlertIo.setEndTime(endTime);
            }

            alertCreatePresenter.createAlert(rideAlertIo, this);
        }
    }

    private boolean isValid()
    {
        boolean isValid = true;

        if(sourceLatLng == null || TextUtils.isEmpty(sourceLocName))
        {
            ViewUtil.t(this, "Choose Source Location");
            isValid = false;
        }
        else if(destLatLng == null || TextUtils.isEmpty(destLocName))
        {
            ViewUtil.t(this, "Choose Destination Location");
            isValid = false;
        }
        else if(startTime != null && endTime != null && startTime.getValue() < endTime.getValue())
        {
            ViewUtil.t(this, "Choose appropriate time");
            isValid = false;
        }

        return isValid;
    }

    @Override
    protected AlertCreatePresenter setCorePresenter()
    {
        return alertCreatePresenter;
    }

    @Override
    public void alertCreated()
    {
        ViewUtil.t(this, "Your alert is created. You will be notified whenever a ride matches your alert. Happy travel");
    }

    @Override
    public void alertCreateFailed()
    {
        ViewUtil.t(this, "Unable to create alert. Make sure you filled the right values. if so sorry for the trouble");
    }
}
