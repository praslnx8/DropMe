package com.prasilabs.dropme.utils;

import com.prasilabs.dropme.R;
import com.prasilabs.dropme.enums.MarkerType;
import com.prasilabs.dropme.pojo.MarkerInfo;

/**
 * Created by prasi on 28/5/16.
 */
public class MarkerUtil
{
    public static String SOURCE_MARKER_KEY = "source_marker";
    public static String DEST_MARKER_KEY = "dest_marker";

    public static int SOURCE_MARKER = 0; //TODO
    public static int DEST_MARKER = 0; //TODO

    public static int getMarkerResId(MarkerInfo markerInfo)
    {
        int resourceID = R.drawable.user_standing;

        if(markerInfo != null)
        {
            if (markerInfo.getMarkerType().equals(MarkerType.Bike.name()))
            {
                resourceID = R.drawable.bike;
            }
            else if(markerInfo.getMarkerType().equals(MarkerType.Car.name()))
            {
                resourceID = R.drawable.ic_car;
            }
            else if(markerInfo.getMarkerType().equals(MarkerType.User.name()))
            {

            }
        }

        return resourceID;
    }
}
