package com.prasilabs.dropme.backend.logicEngines;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.prasilabs.dropme.backend.datastore.DropMeUser;
import com.prasilabs.dropme.backend.datastore.Vehicle;
import com.prasilabs.dropme.backend.db.OfyService;
import com.prasilabs.dropme.backend.debug.ConsoleLog;
import com.prasilabs.dropme.backend.io.ApiResponse;
import com.prasilabs.dropme.backend.io.VVehicle;
import com.prasilabs.util.DataUtil;

/**
 * Created by prasi on 28/5/16.
 */
public class VehicleLogicEngine
{

    private static final String TAG = VehicleLogicEngine.class.getSimpleName();
    private static VehicleLogicEngine vehicleLogicEngine;

    public static VehicleLogicEngine getInstance()
    {
        if(vehicleLogicEngine == null)
        {
            vehicleLogicEngine = new VehicleLogicEngine();
        }

        return vehicleLogicEngine;
    }

    private VehicleLogicEngine(){}

    public ApiResponse addVehicle(User user, VVehicle vVehicle)
    {
        ApiResponse apiResponse = new ApiResponse();

        DropMeUser dropMeUser = DropMeUserLogicEngine.getInstance().getDropMeUser(user.getEmail());
        if(dropMeUser != null)
        {
            vVehicle.setOwnerId(dropMeUser.getId());

            Vehicle vehicle = convertToVehicle(vVehicle);
            boolean isValid = validateVehicle(vehicle);
            if (isValid) {
                Key<Vehicle> vehicleKey = OfyService.ofy().save().entity(vehicle).now();
                apiResponse.setId(vehicleKey.getId());
                apiResponse.setStatus(true);
            } else {
                apiResponse.setMessage("not a valid data");
            }
        }
        else
        {
            apiResponse.setMessage("user is not found");
            ConsoleLog.w(TAG, "user is not found");
        }

        return apiResponse;
    }

    public VVehicle getVehicleById(long id)
    {
        Vehicle vehicle = OfyService.ofy().load().type(Vehicle.class).id(id).now();

        return convertToVVehicle(vehicle);
    }


    private VVehicle convertToVVehicle(Vehicle vehicle)
    {
        if(vehicle != null && vehicle.getId() != null)
        {
            VVehicle vVehicle = new VVehicle();

            vVehicle.setId(vehicle.getId());
            vVehicle.setPicture(vehicle.getPicture());
            vVehicle.setName(vehicle.getName());
            vVehicle.setvNumber(vehicle.getvNumber());
            vVehicle.setDeleted(vehicle.isDeleted());
            vVehicle.setOwnerId(vehicle.getOwnerId());
        }

        return null;
    }

    private Vehicle convertToVehicle(VVehicle vVehicle)
    {
        if(vVehicle != null)
        {
            Vehicle vehicle = new Vehicle();

            vehicle.setId(vVehicle.getId());
            vehicle.setPicture(vVehicle.getPicture());
            vehicle.setName(vVehicle.getName());
            vehicle.setvNumber(vVehicle.getvNumber());
            vehicle.setDeleted(vVehicle.isDeleted());
            vehicle.setOwnerId(vVehicle.getOwnerId());

        }

        return null;
    }

    private boolean validateVehicle(Vehicle vehicle)
    {
        boolean isValid = false;

        if(vehicle != null)
        {
            isValid = true;

            if(DataUtil.isEmpty(vehicle.getName()))
            {
                isValid = false;
            }
            else if(DataUtil.isEmpty(vehicle.getType()))
            {
                isValid = false;
            }
            else if(vehicle.getOwnerId() == 0)
            {
                isValid = false;
            }
            else if(vehicle.getvNumber() == null || !vehicle.getvNumber().isValid())
            {
                isValid = false;
            }
        }

        return isValid;
    }
}
