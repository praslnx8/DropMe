package com.prasilabs.dropme.modules.rideAlerts.view;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.api.client.util.DateTime;
import com.prasilabs.dropme.R;
import com.prasilabs.dropme.backend.dropMeApi.model.GeoPt;
import com.prasilabs.dropme.backend.dropMeApi.model.RideAlertIo;
import com.prasilabs.dropme.core.CoreFragment;
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
 * Created by prasi on 14/6/16.
 */
public class CreateAlertFragment extends CoreFragment<AlertCreatePresenter> implements AlertCreatePresenter.CreateAlertCallBack
{
    private static final String TAG = CreateAlertFragment.class.getSimpleName();
    private static CreateAlertFragment instance;

    public static CreateAlertFragment getInstance()
    {
        if(instance == null)
        {
            instance = new CreateAlertFragment();
        }

        return instance;
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

    @BindView(R.id.timing_btn)
    CheckBox timingBtn;

    @BindView(R.id.start_btn)
    Button startBtn;
    @BindView(R.id.end_btn)
    Button endBtn;

    @BindView(R.id.top_layout)
    LinearLayout topLayout;


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
            setFragmentView(inflater.inflate(R.layout.fragment_create_alert, container, false));

            PlaceAutocompleteFragment sourceLocFragment = (PlaceAutocompleteFragment) getCoreActivity().getFragmentManager().findFragmentById(R.id.source_place_fragment);
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

            PlaceAutocompleteFragment destLocFragment = (PlaceAutocompleteFragment) getCoreActivity().getFragmentManager().findFragmentById(R.id.dest_place_fragment);
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

        return getFragmentView();
    }

    @Override
    protected AlertCreatePresenter setCorePresenter()
    {
        return new AlertCreatePresenter();
    }

    @OnClick(R.id.start_btn)
    protected void onTimeStart()
    {
        final Calendar calendar = Calendar.getInstance();
        if(startTime != null)
        {
            calendar.setTimeInMillis(startTime.getValue());
        }
        else if(endTime != null)
        {
            calendar.setTimeInMillis(endTime.getValue());
        }
        RangeTimePickerDialog rangeTimePickerDialog = new RangeTimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {

                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                startTime = new DateTime(calendar.getTime());

                String ampm = "PM";
                if(calendar.get(Calendar.AM_PM) == 0)
                {
                    ampm = "AM";
                }

                startBtn.setText(calendar.get(Calendar.HOUR) + " : " + minute + " : " + ampm);

            }
        }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), false);

        if(endTime != null)
        {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTimeInMillis(endTime.getValue());
            rangeTimePickerDialog.setMax(calendar1.get(Calendar.HOUR_OF_DAY), calendar1.get(Calendar.MINUTE));
        }

        rangeTimePickerDialog.setTitle("Choose alert start time");
        rangeTimePickerDialog.setMessage("Choose the alert starting time");

        rangeTimePickerDialog.show();
    }

    @OnClick(R.id.end_btn)
    protected void onTimeEnd()
    {
        final Calendar calendar = Calendar.getInstance();
        if(endTime != null)
        {
            calendar.setTimeInMillis(endTime.getValue());
        }
        else if(startTime != null)
        {
            calendar.setTimeInMillis(startTime.getValue());
        }
        RangeTimePickerDialog rangeTimePickerDialog = new RangeTimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {

                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                endTime = new DateTime(calendar.getTime());

                String ampm = "PM";
                if(calendar.get(Calendar.AM_PM) == 0)
                {
                    ampm = "AM";
                }

                endBtn.setText(calendar.get(Calendar.HOUR) + " : " + minute + " : " + ampm);

            }
        }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), false);

        if(startTime != null)
        {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTimeInMillis(startTime.getValue());
            rangeTimePickerDialog.setMin(calendar1.get(Calendar.HOUR_OF_DAY), calendar1.get(Calendar.MINUTE));
        }
        rangeTimePickerDialog.setTitle("Choose End time");
        rangeTimePickerDialog.setMessage("Choose the alert end time");

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

            ViewUtil.showProgressView(getContext(), topLayout, true);
            getPresenter().createAlert(rideAlertIo, CreateAlertFragment.this);
        }
    }

    private boolean isValid()
    {
        boolean isValid = true;

        if(sourceLatLng == null || TextUtils.isEmpty(sourceLocName))
        {
            ViewUtil.t(getContext(), "Choose Source Location");
            isValid = false;
        }
        else if(destLatLng == null || TextUtils.isEmpty(destLocName))
        {
            ViewUtil.t(getContext(), "Choose Destination Location");
            isValid = false;
        }
        else if(timingBtn.isChecked() && startTime != null && endTime != null && startTime.getValue() > endTime.getValue())
        {
            ViewUtil.t(getContext(), "Choose appropriate time");
            isValid = false;
        }

        return isValid;
    }

    @Override
    public void alertCreated()
    {
        ViewUtil.t(getContext(), "Your alert is created. You will be notified whenever a ride matches your alert. Happy travel");
        getCoreActivity().finish();
    }

    @Override
    public void alertCreateFailed()
    {
        ViewUtil.t(getContext(), "Unable to create alert. Make sure you filled the right values. if so sorry for the trouble");
        ViewUtil.hideProgressView(getContext(), topLayout);
    }
}
