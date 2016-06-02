package com.prasilabs.dropme.modules.rideSelect.presenters;

import android.content.Context;
import android.content.Intent;

import com.prasilabs.dropme.backend.dropMeApi.model.RideDetail;
import com.prasilabs.dropme.core.CorePresenter;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.modelengines.RideModelEngine;

import java.util.List;

/**
 * Created by prasi on 30/5/16.
 */
public class RideSelectPresenter extends CorePresenter
{
    private static final String TAG = RideSelectPresenter.class.getSimpleName();

    public RideSelectPresenter(){}

    public void getRideDetailList(final GetRidesCallBack getRidesCallBack)
    {
        ConsoleLog.i(TAG, "ride select called on the presenter");
        RideModelEngine.getInstance().getRideDetailsList(new RideModelEngine.GetRideDetailListCallBack()
        {
            @Override
            public void getRideDetailList(List<RideDetail> rideDetailList)
            {
                if(getRidesCallBack != null)
                {
                    getRidesCallBack.getRides(rideDetailList);
                }
            }
        });
    }

    @Override
    protected void onCreateCalled()
    {

    }

    @Override
    protected void broadCastRecieved(Context context, Intent intent) {

    }

    public interface GetRidesCallBack
    {
        void getRides(List<RideDetail> rideDetailList);
    }
}
