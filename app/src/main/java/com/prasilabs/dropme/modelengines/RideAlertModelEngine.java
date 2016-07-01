package com.prasilabs.dropme.modelengines;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.prasilabs.dropme.backend.dropMeApi.model.ApiResponse;
import com.prasilabs.dropme.backend.dropMeApi.model.RideAlertIo;
import com.prasilabs.dropme.backend.dropMeApi.model.RideAlertIoCollection;
import com.prasilabs.dropme.constants.BroadCastConstant;
import com.prasilabs.dropme.core.CoreApp;
import com.prasilabs.dropme.core.CoreModelEngine;
import com.prasilabs.dropme.services.network.CloudConnect;
import com.prasilabs.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prasi on 9/6/16.
 */
public class RideAlertModelEngine extends CoreModelEngine
{
    private static RideAlertModelEngine instance;

    private List<RideAlertIo> myAlertList = new ArrayList<>();

    private RideAlertModelEngine(){}

    public static RideAlertModelEngine getInstance()
    {
        if(instance == null)
        {
            instance = new RideAlertModelEngine();
        }

        return instance;
    }

    public void createRideAlert(final RideAlertIo rideAlertIo, final CreateAlertCallBack createAlertCallBack)
    {
        callAsync(new AsyncCallBack() {
            @Override
            public ApiResponse async() throws Exception
            {
                return CloudConnect.callDropMeApi(false).createAlert(rideAlertIo).execute();
            }

            @Override
            public <T> void result(T t)
            {
                ApiResponse apiResponse = (ApiResponse) t;

                if(apiResponse != null && apiResponse.getStatus() != null && apiResponse.getStatus())
                {
                    rideAlertIo.setId(apiResponse.getId());
                    myAlertList.add(0,rideAlertIo);

                    Intent intent = new Intent();
                    intent.setAction(BroadCastConstant.ALERT_LIST_REFRESH_INTENT);
                    LocalBroadcastManager.getInstance(CoreApp.getAppContext()).sendBroadcast(intent);
                }

                if(createAlertCallBack != null)
                {
                    createAlertCallBack.alertCreated(apiResponse);
                }
            }

            @Override
            public void error(int errorCode) {

            }
        });
    }

    public void getMyAlerts(final int skip, final int pageSize, boolean isRefresh, final GetAlertCallBack getAlertCallBack)
    {
        if(isRefresh || (myAlertList.size() <= (skip*pageSize)))
        {
            callAsync(new AsyncCallBack() {
                @Override
                public List<RideAlertIo> async() throws Exception
                {

                    RideAlertIoCollection rideAlertIoCollection = CloudConnect.callDropMeApi(false).getAlertListOfUser().execute();
                    if (rideAlertIoCollection != null)
                    {
                        return rideAlertIoCollection.getItems();
                    }

                    return null;
                }

                @Override
                public <T> void result(T t)
                {
                    List<RideAlertIo> rideAlertIoList = (List<RideAlertIo>) t;
                    if(rideAlertIoList != null)
                    {
                        myAlertList.addAll(rideAlertIoList);
                    }

                    if(getAlertCallBack != null)
                    {
                        getAlertCallBack.getAlertList(skip, rideAlertIoList);
                    }
                }

                @Override
                public void error(int errorCode) {

                }
            });
        }
        else
        {
            List<RideAlertIo> myRideInfos = CommonUtil.getSubList(skip,pageSize,myAlertList);

            if(getAlertCallBack != null)
            {
                getAlertCallBack.getAlertList(skip, myRideInfos);
            }
        }
    }

    public void deleteAlert(final long id, final DeleteAlertCallBack deleteAlertCallBack) {
        callAsync(new AsyncCallBack() {
            @Override
            public ApiResponse async() throws Exception {
                return CloudConnect.callDropMeApi(false).deleteAlert(id).execute();
            }

            @Override
            public <T> void result(T t) {
                ApiResponse apiResponse = (ApiResponse) t;

                if (deleteAlertCallBack != null) {
                    if (apiResponse != null) {
                        deleteAlertCallBack.onResult(apiResponse.getStatus());
                    } else {
                        deleteAlertCallBack.onResult(false);
                    }
                }
            }

            @Override
            public void error(int errorCode) {

            }
        });
    }

    public interface CreateAlertCallBack
    {
        void alertCreated(ApiResponse apiResponse);
    }

    public interface GetAlertCallBack
    {
        void getAlertList(int skip, List<RideAlertIo> rideAlertIoList);
    }

    public interface DeleteAlertCallBack {
        void onResult(boolean status);
    }
}
