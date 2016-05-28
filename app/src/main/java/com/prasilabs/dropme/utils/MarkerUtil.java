package com.prasilabs.dropme.utils;

import com.prasilabs.dropme.enums.MarkerType;
import com.prasilabs.dropme.pojo.MarkerInfo;

/**
 * Created by prasi on 28/5/16.
 */
public class MarkerUtil
{
    public static int getMarkerResId(MarkerInfo markerInfo)
    {
        int resourceID = 0;

        if(markerInfo != null)
        {
            if (markerInfo.getMarkerType().equals(MarkerType.Bike.name()))
            {
                //TODO
            }
        }

        return resourceID;
    }
}
