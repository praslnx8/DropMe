package com.prasilabs.dropme.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by prasi on 26/5/16.
 */
public class ViewUtil
{
    public static void t(Context context, String message)
    {
        if(context != null && !TextUtils.isEmpty(message))
        {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }
}
