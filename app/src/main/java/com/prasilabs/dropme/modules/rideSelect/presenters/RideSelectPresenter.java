package com.prasilabs.dropme.modules.rideSelect.presenters;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;
import com.prasilabs.dropme.backend.dropMeApi.model.RideDetail;
import com.prasilabs.dropme.core.CorePresenter;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.modelengines.RideModelEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prasi on 30/5/16.
 */
public class RideSelectPresenter extends CorePresenter
{
    private static final String TAG = RideSelectPresenter.class.getSimpleName();
    private RenderViewCallBack renderViewCallBack;

    private List<RideDetail> persistentAllRideDetailList = new ArrayList<>();
    private List<RideDetail> persistentDestRideDetailList = new ArrayList<>();

    public RideSelectPresenter(RenderViewCallBack renderViewCallBack)
    {
        this.renderViewCallBack = renderViewCallBack;
    }

    public void getRideDetailList(final GetRidesCallBack getRidesCallBack)
    {
        ConsoleLog.i(TAG, "ride select called on the presenter");
        RideModelEngine.getInstance().getRideDetailsList(null, new RideModelEngine.GetRideDetailListCallBack()
        {
            @Override
            public void getRideDetailList(List<RideDetail> rideDetailList)
            {
                persistentAllRideDetailList.clear();
                persistentAllRideDetailList.addAll(rideDetailList);
                if(getRidesCallBack != null)
                {
                    getRidesCallBack.getRides(rideDetailList);
                }
            }
        });
    }

    public void filterByDestination(String dest, LatLng latLng, final GetRidesCallBack getRidesCallBack)
    {
        RideFilter.getInstance().setDestination(dest);
        RideFilter.getInstance().setDestPt(latLng);

        if(renderViewCallBack != null)
        {
            renderViewCallBack.renderFilterData();
        }

        ConsoleLog.i(TAG, "ride select called on the presenter");
        RideModelEngine.getInstance().getRideDetailsList(latLng, new RideModelEngine.GetRideDetailListCallBack()
        {
            @Override
            public void getRideDetailList(List<RideDetail> rideDetailList)
            {
                persistentDestRideDetailList.clear();
                persistentDestRideDetailList.addAll(rideDetailList);
                if(getRidesCallBack != null)
                {
                    getRidesCallBack.getRides(rideDetailList);
                }
            }
        });
    }

    public void filter(final GetRidesCallBack getRidesCallBack)
    {
        if(renderViewCallBack != null)
        {
            renderViewCallBack.renderFilterData();
        }

        List<RideDetail> filteredList = new ArrayList<>();

        RideFilter rideFilter = RideFilter.getInstance();

        List<RideDetail> rideDetailList;
        if(rideFilter.getDestination() != null && rideFilter.getDestPt() != null)
        {
            rideDetailList = persistentDestRideDetailList;
        }
        else
        {
            rideDetailList = persistentAllRideDetailList;
        }

        for(RideDetail rideDetail : rideDetailList)
        {
            boolean isValid = true;
            try {
                if (rideFilter.getGender() != null)
                {
                    if (!rideDetail.getGender().equals(rideFilter.getGender().name())) {
                        isValid = false;
                    }
                }

                if (rideFilter.getvType() != null)
                {
                    if (!rideDetail.getVehicleType().equals(rideFilter.getvType().name())) {
                        isValid = false;
                    }
                }
            }catch (Exception e)
            {
                ConsoleLog.e(e);
            }

            if(isValid)
            {
                filteredList.add(rideDetail);
            }
        }

        if(getRidesCallBack != null)
        {
            getRidesCallBack.getRides(filteredList);
        }
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

    public interface RenderViewCallBack
    {
        void renderFilterData();
    }
}
