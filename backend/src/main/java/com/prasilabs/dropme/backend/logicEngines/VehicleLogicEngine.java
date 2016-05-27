package com.prasilabs.dropme.backend.logicEngines;

/**
 * Created by prasi on 28/5/16.
 */
public class VehicleLogicEngine
{

    private static VehicleLogicEngine vehicleLogicEngine;

    public static VehicleLogicEngine getInstance()
    {
        if(vehicleLogicEngine == null)
        {
            vehicleLogicEngine = new VehicleLogicEngine();
        }

        return vehicleLogicEngine;
    }
}
