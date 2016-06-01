package com.prasilabs.dropme.utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.prasilabs.dropme.backend.dropMeApi.model.GeoPt;

import java.text.NumberFormat;

/**
 * Created by prasi on 31/5/16.
 */
public class LocationUtils
{
    private static final String DISTANCE_KM_POSTFIX = "KM";
    private static final String DISTANCE_M_POSTFIX = "M";

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

    public static String formatDistanceBetween(LatLng point1, LatLng point2) {
        if (point1 == null || point2 == null) {
            return null;
        }

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        double distance = Math.round(SphericalUtil.computeDistanceBetween(point1, point2));

        // Adjust to KM if M goes over 1000 (see javadoc of method for note
        // on only supporting metric)
        if (distance >= 100) {
            numberFormat.setMaximumFractionDigits(1);
            return numberFormat.format(distance / 1000) + DISTANCE_KM_POSTFIX;
        }
        return numberFormat.format(distance) + DISTANCE_M_POSTFIX;
    }
}
