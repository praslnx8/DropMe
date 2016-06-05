package com.prasilabs.dropme.backend.utils;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.prasilabs.dropme.backend.debug.ConsoleLog;

import java.util.ArrayList;
import java.util.List;

import static java.lang.StrictMath.cos;

/**
 * Created by prasi on 6/4/16.
 */
public class GeoFilter
{
    //with reference to http://stackoverflow.com/questions/7477003/calculating-new-longtitude-latitude-from-old-n-meters

    private static final String TAG = GeoFilter.class.getSimpleName();

    private static final long EARTH_RADIUS_METRE = 6371 * 1000;
    private static final double pi = 3.14159265359;

    private static final String LAT_STR =  ".latitude";
    private static final String LON_STR =  ".longitude";

    private String key;
    private String latKey;
    private String lonKey;

    public GeoFilter(String key)
    {
        this.key = key;
        this.latKey = key + LAT_STR;
        this.lonKey = key + LON_STR;
    }

    public Filter getFilter(GeoPt geoPt, double radiusInMetre)
    {
        if(key == null || geoPt == null)
        {
            throw new IllegalArgumentException("passing null values");
        }

        double extendedLat  = geoPt.getLatitude()  + (radiusInMetre / EARTH_RADIUS_METRE) * (180 / pi);
        double extendedLon = geoPt.getLongitude() + (radiusInMetre / EARTH_RADIUS_METRE) * (180 / pi) / cos(geoPt.getLatitude() * pi/180);

        ConsoleLog.i(TAG, "new lat is : " + extendedLat);
        ConsoleLog.i(TAG, "new lon is : " + extendedLon);

        Filter latFilter = new Query.FilterPredicate(latKey, Query.FilterOperator.LESS_THAN_OR_EQUAL, extendedLat);
        Filter lonFilter = new Query.FilterPredicate(lonKey, Query.FilterOperator.LESS_THAN_OR_EQUAL, extendedLon);

        List<Filter> filterList = new ArrayList<>();
        filterList.add(latFilter);
        filterList.add(lonFilter);

        Filter filter = new Query.CompositeFilter(Query.CompositeFilterOperator.AND, filterList);

        return filter;
    }
}
