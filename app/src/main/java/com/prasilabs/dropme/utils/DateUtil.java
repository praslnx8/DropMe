package com.prasilabs.dropme.utils;

import android.text.TextUtils;
import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by prasi on 26/5/16.
 */
public class DateUtil
{
    public static String getRelativeTime(String datetime) {
        long timeInMillis = System.currentTimeMillis();
        if (!TextUtils.isEmpty(datetime))
        {
            // Or a mysql datetime. Hence handling both.
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = null;
            try {
                d = f.parse(datetime);
            } catch (Exception e) {

            }

            try {
                if (d != null) {
                    timeInMillis = d.getTime();
                } else {
                    timeInMillis = Long.valueOf(datetime);
                }
            } catch (NumberFormatException nfe) {
            }
        }
        return getRelativeTime(timeInMillis);
    }

    public static String getRelativeTime(long timeInMillis) {
        CharSequence relativeTimeSpanString = null;
        try {
            // Time comes either as millisecond values from search or string from mysql
            timeInMillis = Long.valueOf(timeInMillis);
        }
        catch (NumberFormatException ne)
        {
            return "";
        }
        relativeTimeSpanString = DateUtils.getRelativeTimeSpanString(timeInMillis, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE);
        if (relativeTimeSpanString != null) {
            return relativeTimeSpanString.toString();
        } else {
            return "";
        }
    }
}
