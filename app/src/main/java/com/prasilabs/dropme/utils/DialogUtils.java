package com.prasilabs.dropme.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.prasilabs.dropme.backend.dropMeApi.model.RideDetail;
import com.prasilabs.dropme.debug.ConsoleLog;

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
        CharSequence[] menuOption = new CharSequence[]{"Call", "View Profile", "Show In Map"};
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
                else if(which == 1)
                {
                    ViewUtil.t(context, "TODO. Profile page");
                }
                else if(which == 2)
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
}
