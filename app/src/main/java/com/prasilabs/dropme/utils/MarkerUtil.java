package com.prasilabs.dropme.utils;

import com.prasilabs.dropme.R;
import com.prasilabs.dropme.enums.MarkerType;
import com.prasilabs.dropme.pojo.MarkerInfo;

/**
 * Created by prasi on 28/5/16.
 */
public class MarkerUtil
{
    public static int getMarkerResId(MarkerInfo markerInfo)
    {
        int resourceID = R.drawable.user;

        if(markerInfo != null)
        {
            if (markerInfo.getMarkerType().equals(MarkerType.Bike.name()))
            {
                //TODO
            }
            else if(markerInfo.getMarkerType().equals(MarkerType.Biker.name()))
            {

            }
            else if(markerInfo.getMarkerType().equals(MarkerType.Car.name()))
            {

            }
            else if(markerInfo.getMarkerType().equals(MarkerType.User.name()))
            {

            }
        }

        return resourceID;
    }
}
