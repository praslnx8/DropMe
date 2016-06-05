package com.prasilabs.dropme.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RadioButton;

import com.prasilabs.dropme.R;
import com.prasilabs.dropme.backend.dropMeApi.model.RideDetail;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.modules.rideSelect.presenters.RideFilter;
import com.prasilabs.enums.Gender;
import com.prasilabs.enums.VehicleType;

/**
 * Created by prasi on 4/6/16.
 */
public class DialogUtils
{
    private static final String TAG = DialogUtils.class.getSimpleName();

    public static void showSelectRideMenu(final Context context, final RideDetail rideDetail)
    {
        ConsoleLog.i(TAG, "showing menu called");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        CharSequence[] menuOption = new CharSequence[]{"Call" /*"View Profile",*/ /*"Show In Map"*/};
        builder.setItems(menuOption, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(which == 0)
                {
                    String phoneUrl = "tel:" + rideDetail.getOwnerPhone();
                    Intent in = new Intent(Intent.ACTION_CALL, Uri.parse(phoneUrl));
                    try
                    {
                        context.startActivity(in);
                    }
                    catch (android.content.ActivityNotFoundException ex)
                    {
                        ViewUtil.t(context, "You cannot make phone calls in this phone");
                    }
                }
                /*else if(which == 1)
                {
                    ViewUtil.t(context, "TODO. Profile page");
                }*/
                else if(which == 1)
                {
                    ViewUtil.t(context, "TODO. Map page");
                }
                else
                {
                    ConsoleLog.w(TAG, "inappropriate selection");
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showRideSelectFilter(final Context context, final FilterDialogCallBack filterDialogCallBack)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Filter Rides");

        View view = View.inflate(context, R.layout.dialog_filter, null);

        final RadioButton maleBtn = (RadioButton) view.findViewById(R.id.male_btn);
        final RadioButton feMaleBtn = (RadioButton) view.findViewById(R.id.female_btn);
        final RadioButton carBtn = (RadioButton) view.findViewById(R.id.car_btn);
        final RadioButton bikeBtn = (RadioButton) view.findViewById(R.id.bike_btn);

        final RideFilter rideFilter = RideFilter.getInstance();

        if(rideFilter.getGender() != null)
        {
            if(rideFilter.getGender() == Gender.Male)
            {
                maleBtn.setChecked(true);
                feMaleBtn.setChecked(false);
            }
            else if(rideFilter.getGender() == Gender.Female)
            {
                maleBtn.setChecked(false);
                feMaleBtn.setChecked(true);
            }
        }

        if(rideFilter.getvType() != null)
        {
            if(rideFilter.getvType() == VehicleType.Car)
            {
                carBtn.setChecked(true);
                bikeBtn.setChecked(false);
            }
            else if(rideFilter.getvType() == VehicleType.Bike)
            {
                carBtn.setChecked(false);
                bikeBtn.setChecked(true);
            }
        }

        builder.setView(view);

        builder.setPositiveButton("Filter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                ConsoleLog.i(TAG, "byutton clicked");
                if(maleBtn.isChecked())
                {
                    rideFilter.setGender(Gender.Male);
                }
                else if(feMaleBtn.isChecked())
                {
                    rideFilter.setGender(Gender.Female);
                }

                if(carBtn.isChecked())
                {
                    rideFilter.setvType(VehicleType.Car);
                }
                else if(bikeBtn.isChecked())
                {
                    rideFilter.setvType(VehicleType.Bike);
                }

                if(filterDialogCallBack != null)
                {
                    filterDialogCallBack.filterApplied();
                }
            }
        });

        builder.create().show();

    }

    public interface FilterDialogCallBack
    {
        void filterApplied();
    }
}
