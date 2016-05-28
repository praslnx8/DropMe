package com.prasilabs.dropme.managers;

import com.prasilabs.dropme.backend.dropMeApi.model.VVehicle;
import com.prasilabs.dropme.enums.RideStatus;

/**
 * Created by prasi on 28/5/16.
 */
public class RideManager
{
    public static RideStatus rideStatus = RideStatus.User;
    private static VVehicle currentVehicle;

    public static VVehicle getCurentOfferingVehicle()
    {
        return currentVehicle;
    }

    public static void setCurrentVehicle(VVehicle vVehicle)
    {
        currentVehicle = vVehicle;
    }
}
