package com.prasilabs.dropme.backend.utils;

import com.google.appengine.api.datastore.GeoPt;

/**
 * Created by kiran on 12/9/2015.
 */

public class DistanceCalculator
{
	private static final String TAG = DistanceCalculator.class.getSimpleName();
	//        System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, "M") + " Miles\n");
//        System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, "K") + " Kilometers\n");
//        System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, "N") + " Nautical Miles\n");

	public static boolean isFit(GeoPt geoPt1, GeoPt geoPt2, double distanceInKM)
	{
		if(geoPt1 != null && geoPt2 != null)
		{
			Float lat1 = geoPt1.getLatitude();
			Float lon1 = geoPt1.getLongitude();
			Float lat2 = geoPt2.getLatitude();
			Float lon2 = geoPt2.getLongitude();
			double distance  = DistanceCalculator.distance(lat1.doubleValue(), lon1.doubleValue(), lat2.doubleValue(), lon2.doubleValue(), "K");
			if(distance < distanceInKM)
			{
				return true;
			}
		}

		return false;
	}


	public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit.equals("K")) {
			dist = dist * 1.609344;
		} else if (unit.equals("N")) {
			dist = dist * 0.8684;
		}

		return (dist);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts decimal degrees to radians						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts radians to decimal degrees						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
}
