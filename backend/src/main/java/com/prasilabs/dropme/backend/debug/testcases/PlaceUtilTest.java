package com.prasilabs.dropme.backend.debug.testcases;

import com.google.appengine.api.datastore.GeoPt;
import com.prasilabs.dropme.backend.services.places.PlaceUtil;

import org.junit.Test;

/**
 * Created by prasi on 13/6/16.
 */
public class PlaceUtilTest
{
    @Test
    public void getPlaceUtil()
    {
        Double lat = 12.969394;
        Double lon = 77.643863;
        GeoPt geoPt = new GeoPt(lat.floatValue(), lon.floatValue());
        String loc = PlaceUtil.getLocalityName(geoPt);
        System.out.print(loc);
    }

}
