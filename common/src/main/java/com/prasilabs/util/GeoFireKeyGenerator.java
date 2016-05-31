package com.prasilabs.util;

/**
 * Created by prasi on 1/6/16.
 */
public class GeoFireKeyGenerator
{
    private static final String GeoRideStr = "veh";
    private static final String splitter = "-";

    public static String generateRideKey(long id)
    {
        return GeoRideStr + splitter + id;
    }
}
