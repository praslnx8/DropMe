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
        int resourceID = android.R.drawable.ic_delete;

        if(markerInfo != null)
        {
            if (markerInfo.getMarkerType().equals(MarkerType.Bike.name()))
            {
                resourceID = R.drawable.ic_marker_bike;
            }
            else if(markerInfo.getMarkerType().equals(MarkerType.Car.name()))
            {
                resourceID = R.drawable.ic_marker_car;
            }
            else if(markerInfo.getMarkerType().equals(MarkerType.UserMale.name()))
            {
                resourceID = R.drawable.ic_person;
            }
            else if(markerInfo.getMarkerType().equals(MarkerType.UserFeMale.name()))
            {
                resourceID = R.drawable.ic_marker_female;
            }
        }

        return resourceID;
    }
}
