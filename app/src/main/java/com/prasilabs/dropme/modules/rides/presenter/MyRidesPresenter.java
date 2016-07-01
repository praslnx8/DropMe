package com.prasilabs.dropme.modules.rides.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.prasilabs.constants.CommonConstant;
import com.prasilabs.dropme.backend.dropMeApi.model.MyRideInfo;
import com.prasilabs.dropme.constants.BroadCastConstant;
import com.prasilabs.dropme.core.CorePresenter;
import com.prasilabs.dropme.modelengines.RideModelEngine;

import java.util.List;

/**
 * Created by prasi on 10/6/16.
 */
public class MyRidesPresenter extends CorePresenter
{
    private int skip;
    private MyRideListCallBack myRideListCallBack;

    public MyRidesPresenter(MyRideListCallBack myRideListCallBack)
    {
        this.myRideListCallBack = myRideListCallBack;
    }

    @Override
    protected void onCreateCalled()
    {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadCastConstant.RIDE_ADDED_INTENT);
        registerReciever(intentFilter);
    }

    @Override
    protected void broadCastRecieved(Context context, Intent intent)
    {
        if(intent.getAction().equals(BroadCastConstant.RIDE_ADDED_INTENT))
        {
            getMyRides(false, skip);
        }
    }

    public void getMyRides(boolean isRefresh, int skip)
    {
        this.skip = skip;
        RideModelEngine.getInstance().getMyRideList(isRefresh, skip, CommonConstant.PAGE_SIZE, new RideModelEngine.GetRideInfoListCallBack() {
            @Override
            public void myRideInfoList(List<MyRideInfo> myRideInfoList, int skip)
            {
                if(myRideListCallBack != null)
                {
                    if(myRideInfoList != null && myRideInfoList.size() > 0)
                    {
                        myRideListCallBack.getMyRideList(skip, myRideInfoList);
                    }
                    else
                    {
                        myRideListCallBack.getMyRideIsEmpty(skip);
                    }
                }
            }

            @Override
            public void error(int errorCode) {
                if (myRideListCallBack != null) {
                    myRideListCallBack.showNoInternet();
                }
            }
        });
    }

    public interface MyRideListCallBack
    {
        void getMyRideList(int skip, List<MyRideInfo> myRideInfoList);

        void getMyRideIsEmpty(int skip);

        void showNoInternet();
    }

}
