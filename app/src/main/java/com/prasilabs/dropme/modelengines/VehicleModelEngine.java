package com.prasilabs.dropme.modelengines;

import com.prasilabs.dropme.backend.dropMeApi.model.ApiResponse;
import com.prasilabs.dropme.backend.dropMeApi.model.VDropMeUser;
import com.prasilabs.dropme.backend.dropMeApi.model.VVehicle;
import com.prasilabs.dropme.core.CoreApp;
import com.prasilabs.dropme.core.CoreModelEngine;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.managers.UserManager;
import com.prasilabs.dropme.services.network.CloudConnect;

/**
 * Created by prasi on 27/5/16.
 */
public class VehicleModelEngine extends CoreModelEngine
{
    private static VehicleModelEngine instance;

    public static VehicleModelEngine getInstance()
    {
        if(instance == null)
        {
            instance = new VehicleModelEngine();
        }

        return instance;
    }

    private VehicleModelEngine(){}

    public void addVehicle(final VVehicle vVehicle, final VehicleAddCallBack vehicleAddCallBack)
    {
        callAsync(new AsyncCallBack() {
            @Override
            public ApiResponse async()
            {
                try
                {
                    VDropMeUser vDropMeUser = UserManager.getDropMeUser(CoreApp.getAppContext());
                    return CloudConnect.callDropMeApi(false).addVehicle(vVehicle).execute();
                }
                catch (Exception e)
                {
                    ConsoleLog.e(e);
                }

                return null;
            }

            @Override
            public <T> void result(T t)
            {
                if(vehicleAddCallBack != null)
                {
                    vehicleAddCallBack.addVehicle((ApiResponse) t);
                }
            }
        });
    }

    public void getVehicleDetail(final long id, final VehicleGetCallBack vehicleGetCallBack)
    {
        callAsync(new AsyncCallBack() {
            @Override
            public VVehicle async()
            {
                try
                {
                    return CloudConnect.callDropMeApi(false).getVehicleDetail(id).execute();
                }
                catch (Exception e)
                {
                    ConsoleLog.e(e);
                }

                return null;
            }

            @Override
            public <T> void result(T t)
            {
                if(vehicleGetCallBack != null)
                {
                    vehicleGetCallBack.getVehicle((VVehicle) t);
                }
            }
        });
    }

    public interface VehicleGetCallBack
    {
        void getVehicle(VVehicle vVehicle);
    }

    public interface VehicleAddCallBack
    {
        void addVehicle(ApiResponse apiResponse);
    }
}
