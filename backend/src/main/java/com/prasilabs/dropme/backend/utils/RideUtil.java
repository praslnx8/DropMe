package com.prasilabs.dropme.backend.utils;

import com.google.appengine.api.datastore.GeoPt;
import com.prasilabs.dropme.backend.datastore.Ride;
import com.prasilabs.dropme.backend.debug.ConsoleLog;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by prasi on 31/5/16.
 */
public class RideUtil
{
    private static final int MIN_SPEED = 10; //km/hr
    private static final double speed_in_meters_per_minute = ( MIN_SPEED * 1000 ) / 60; // mpm
    private static final String TAG = RideUtil.class.getSimpleName();

    public static void calculateAndSetExpiry(Ride ride)
    {
        if(ride != null)
        {
            GeoPt source = ride.getSourceLoc();
            GeoPt dest = ride.getDestLoc();

            double distance = DistanceCalculator.distance(source.getLatitude(), source.getLongitude(), dest.getLatitude(), dest.getLongitude(), "K");

            ConsoleLog.i(TAG, "distance is : " + distance);
            double timeInMins = (double) distance*1000 / speed_in_meters_per_minute;

            ConsoleLog.i(TAG, " time in mins is : " + timeInMins);

            Date startDate = ride.getStartDate();
            if (startDate == null) {
                startDate = new Date(System.currentTimeMillis());
            }
            ConsoleLog.i(TAG, "start time is :" + startDate.toString());


            Date expiryDate = new Date(startDate.getTime());
            ConsoleLog.i(TAG, "now time is : " + expiryDate.toString());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(expiryDate);
            calendar.add(Calendar.MINUTE, (int) timeInMins);
            expiryDate = calendar.getTime();

            ConsoleLog.i(TAG, "expity time is : " + expiryDate.toString());

            ride.setExpiryDate(expiryDate);
        }
    }
}
