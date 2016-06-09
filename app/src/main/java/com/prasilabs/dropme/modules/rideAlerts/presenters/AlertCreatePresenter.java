package com.prasilabs.dropme.modules.rideAlerts.presenters;

import android.content.Context;
import android.content.Intent;

import com.prasilabs.dropme.backend.dropMeApi.model.ApiResponse;
import com.prasilabs.dropme.backend.dropMeApi.model.RideAlertIo;
import com.prasilabs.dropme.core.CorePresenter;
import com.prasilabs.dropme.modelengines.RideAlertModelEngine;

/**
 * Created by prasi on 9/6/16.
 */
public class AlertCreatePresenter extends CorePresenter
{

    @Override
    protected void onCreateCalled()
    {

    }

    public void createAlert(RideAlertIo rideAlertIo, final CreateAlertCallBack createAlertCallBack)
    {
        RideAlertModelEngine.getInstance().createRideAlert(rideAlertIo, new RideAlertModelEngine.CreateAlertCallBack() {
            @Override
            public void alertCreated(ApiResponse apiResponse)
            {
                if(createAlertCallBack != null)
                {
                    if(apiResponse != null && apiResponse.getStatus() != null && apiResponse.getStatus())
                    {
                        createAlertCallBack.alertCreated();
                    }
                    else
                    {
                        createAlertCallBack.alertCreateFailed();
                    }
                }

            }
        });
    }

    @Override
    protected void broadCastRecieved(Context context, Intent intent)
    {

    }

    public interface CreateAlertCallBack
    {
        void alertCreated();

        void alertCreateFailed();
    }
}
