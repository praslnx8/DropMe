package com.prasilabs.dropme.modules.rideCreate.presenter;

import android.content.Context;
import android.content.Intent;

import com.prasilabs.dropme.backend.dropMeApi.model.RideInput;
import com.prasilabs.dropme.core.CorePresenter;
import com.prasilabs.dropme.modelengines.RideModelEngine;

/**
 * Created by prasi on 31/5/16.
 */
public class RideCreatePresenter extends CorePresenter
{
    private RideCreatePresenterCallBack rideCreatePresenterCallBack;

    public RideCreatePresenter(RideCreatePresenterCallBack rideCreatePresenterCallBack)
    {
        this.rideCreatePresenterCallBack = rideCreatePresenterCallBack;
    }

    @Override
    protected void onCreateCalled()
    {

    }

    public void createRide(final RideInput rideInput)
    {
        RideModelEngine.getInstance().createRide(rideInput, new RideModelEngine.RideCreateCallBack() {
            @Override
            public void rideCreated()
            {
                rideCreatePresenterCallBack.rideCreated(rideInput);
            }

            @Override
            public void rideCreateFailed()
            {
                rideCreatePresenterCallBack.rideCreateFailed();
            }
        });
    }


    @Override
    protected void broadCastRecieved(Context context, Intent intent)
    {

    }

    public interface RideCreatePresenterCallBack
    {
        void rideCreated(RideInput rideInput);

        void rideCreateFailed();
    }
}
