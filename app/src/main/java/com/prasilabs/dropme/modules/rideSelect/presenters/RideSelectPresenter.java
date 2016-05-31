package com.prasilabs.dropme.modules.rideSelect.presenters;

import android.content.Context;
import android.content.Intent;

import com.prasilabs.dropme.backend.dropMeApi.model.RideDetail;
import com.prasilabs.dropme.core.CorePresenter;
import com.prasilabs.dropme.modelengines.RideModelEngine;

import java.util.List;

/**
 * Created by prasi on 30/5/16.
 */
public class RideSelectPresenter extends CorePresenter
{

    public static RideSelectPresenter newInstance()
    {
        return new RideSelectPresenter();
    }

    public void getRideDetailList(final GetRidesCallBack getRidesCallBack)
    {
        RideModelEngine.getInstance().getRideDetailsList(new RideModelEngine.GetRideDetailListCallBack() {
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
