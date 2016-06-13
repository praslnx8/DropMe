package com.prasilabs.dropme.modules.rideAlerts.presenters;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.prasilabs.dropme.backend.dropMeApi.model.RideAlertIo;
import com.prasilabs.dropme.constants.BroadCastConstant;
import com.prasilabs.dropme.core.CorePresenter;
import com.prasilabs.dropme.modelengines.RideAlertModelEngine;

import java.util.List;

/**
 * Created by prasi on 13/6/16.
 */
public class MyAlertsPresenter extends CorePresenter
{
    private AlertCallBack alertCallBack;
    private int skip = 0;
    private int pageSize;

    public MyAlertsPresenter(AlertCallBack alertCallBack)
    {
        this.alertCallBack = alertCallBack;
    }

    @Override
    protected void onCreateCalled()
    {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadCastConstant.ALERT_LIST_REFRESH_INTENT);
        registerReciever(intentFilter);
    }

    @Override
    protected void broadCastRecieved(Context context, Intent intent)
    {
        if(intent.getAction().equals(BroadCastConstant.ALERT_LIST_REFRESH_INTENT))
        {
            getAlerts(0, pageSize, false);
        }
    }

    public void getAlerts(int skip, int pageSize, boolean isRefresh)
    {
        this.skip = skip;
        this.pageSize = pageSize;
        RideAlertModelEngine.getInstance().getMyAlerts(skip, pageSize, isRefresh, new RideAlertModelEngine.GetAlertCallBack() {
            @Override
            public void getAlertList(int skip, List<RideAlertIo> rideAlertIoList)
            {
                if(rideAlertIoList != null && rideAlertIoList.size() > 0 )
                {
                    alertCallBack.getAlertList(skip, rideAlertIoList);
                }
                else
                {
                    alertCallBack.showEmpty(skip);
                }
            }
        });
    }

    public interface AlertCallBack
    {
        void getAlertList(int skip, List<RideAlertIo> rideAlertIoList);

        void showEmpty(int skip);
    }
}
