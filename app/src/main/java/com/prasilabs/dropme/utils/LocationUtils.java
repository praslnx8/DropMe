package com.prasilabs.dropme.utils;

import com.google.android.gms.maps.model.LatLng;
import com.prasilabs.dropme.backend.dropMeApi.model.GeoPt;

/**
 * Created by prasi on 31/5/16.
 */
public class LocationUtils
{
    public static GeoPt convertToGeoPt(LatLng latLng)
    {

        GeoPt geoPt = null;
        if(latLng != null)
        {
            geoPt = new GeoPt();
            Double lat = latLng.latitude;
            Double lon = latLng.longitude;
            geoPt.setLatitude(lat.floatValue());
            geoPt.setLongitude(lon.floatValue());
        }

        return geoPt;
    }

    public static LatLng convertToLatLng(GeoPt geoPt)
    {
        if(geoPt != null)
        {

            Float lat = geoPt.getLatitude();
            Float lon = geoPt.getLongitude();

            LatLng latLng = new LatLng(lat.doubleValue(), lon.doubleValue());

            return latLng;
        }

        return null;
    }
}
