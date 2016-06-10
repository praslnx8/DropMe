package com.prasilabs.dropme.modelengines;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.maps.model.LatLng;
import com.prasilabs.dropme.backend.dropMeApi.model.ApiResponse;
import com.prasilabs.dropme.backend.dropMeApi.model.GeoPt;
import com.prasilabs.dropme.backend.dropMeApi.model.MyRideInfo;
import com.prasilabs.dropme.backend.dropMeApi.model.RideDetail;
import com.prasilabs.dropme.backend.dropMeApi.model.RideInput;
import com.prasilabs.dropme.constants.BroadCastConstant;
import com.prasilabs.dropme.core.CoreApp;
import com.prasilabs.dropme.core.CoreModelEngine;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.managers.RideManager;
import com.prasilabs.dropme.services.network.CloudConnect;
import com.prasilabs.dropme.utils.LocationUtils;
import com.prasilabs.util.CommonUtil;
import com.prasilabs.util.DataUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prasi on 31/5/16.
 */
public class RideModelEngine extends CoreModelEngine
{
    private static final String TAG = RideModelEngine.class.getSimpleName();
    private static RideModelEngine instance;

    private List<MyRideInfo> myRideInfoList = new ArrayList<>();

    public static RideModelEngine getInstance()
    {
        if(instance == null)
        {
            instance = new RideModelEngine();
        }

        return instance;
    }

    public void updateRide(final RideInput rideInput, final RideCreateCallBack rideCreateCallBack)
    {
        callAsync(new AsyncCallBack() {
            @Override
            public ApiResponse async()
            {
                try
                {
                    return CloudConnect.callDropMeApi(false).updateRide(rideInput).execute();
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
                ApiResponse apiResponse  = (ApiResponse) t;

                if(apiResponse != null)
                {
                    rideInput.setId(apiResponse.getId());
                }
                else
                {
                    rideInput.setId(null);
                }

            }
        });
    }

    public void createRide(final RideInput input, final RideCreateCallBack rideCreateCallBack)
    {
        callAsync(new AsyncCallBack()
        {
            @Override
            public RideInput async()
            {
                try
                {
                    RideInput output = CloudConnect.callDropMeApi(false).createRide(input).execute();
                    return output;
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
                RideInput output = (RideInput) t;

                if(output != null && !DataUtil.isEmpty(output.getId()))
                {
                    RideManager.saveRideLite(CoreApp.getAppContext(), output);

                    HomeGeoModelEngine.getInstance().addRidePoint(output);

                    Intent intent = new Intent();
                    intent.setAction(BroadCastConstant.RIDE_REFRESH_INTENT);
                    LocalBroadcastManager.getInstance(CoreApp.getAppContext()).sendBroadcast(intent);
                }
                else
                {
                    ConsoleLog.i(TAG, "ride lite creation has bug :");
                }

                if(rideCreateCallBack != null)
                {
                    if(output != null && output.getId() != null && output.getId() != 0)
                    {
                        rideCreateCallBack.rideCreated();
                    }
                    else
                    {
                        rideCreateCallBack.rideCreateFailed();
                    }
                }
            }
        });
    }

    public void getCurrentRide()
    {
        callAsync(new AsyncCallBack() {
            @Override
            public RideInput async()
            {
                try
                {
                    RideInput rideInput = CloudConnect.callDropMeApi(false).getCurrentRide(CoreApp.getDeviceId()).execute();
                    return rideInput;
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
                RideInput rideInput = (RideInput) t;

                RideManager.saveRideLite(CoreApp.getAppContext(), rideInput);

                Intent intent = new Intent();
                intent.setAction(BroadCastConstant.RIDE_REFRESH_INTENT);
                LocalBroadcastManager.getInstance(CoreApp.getAppContext()).sendBroadcast(intent);
            }
        });
    }

    public void getRideDetail(final long rideId, final RideDetailCallBack rideDetailCallBack)
    {
        ConsoleLog.i(TAG, "id is : " + rideId);
        callAsync(new AsyncCallBack() {
            @Override
            public RideDetail async()
            {
                try
                {
                    ConsoleLog.i(TAG, "id is : " + rideId);
                    return CloudConnect.callDropMeApi(false).getRideDetail(rideId).execute();
                } catch (Exception e) {
                    ConsoleLog.e(e);
                }
                return null;
            }

            @Override
            public <T> void result(T t)
            {
                RideDetail rideDetail = (RideDetail) t;

                if(rideDetailCallBack != null)
                {
                    rideDetailCallBack.getRideDetail(rideDetail);
                }
            }
        });
    }


    public void cancelRide(final CancleRideCallBack cancleRideCallBack)
    {
        callAsync(new AsyncCallBack() {
            @Override
            public ApiResponse async()
            {
                try
                {
                    return CloudConnect.callDropMeApi(false).cancelRide(CoreApp.getDeviceId()).execute();
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
                ApiResponse apiResponse = (ApiResponse) t;
                RideManager.saveRideLite(CoreApp.getAppContext(), null);
                if(cancleRideCallBack != null)
                {
                    cancleRideCallBack.cancel(apiResponse.getStatus());
                }
            }
        });
    }

    public void getRideDetailsList(LatLng dest, final GetRideDetailListCallBack getRideDetailListCallBack)
    {
        final List<Long> idsList = HomeGeoModelEngine.getInstance().getAllRides();
        final GeoPt geoPt = LocationUtils.convertToGeoPt(dest);

        if(idsList.size() > 0)
        {
            callAsync(new AsyncCallBack() {
                @Override
                public List<RideDetail> async() {
                    try {
                        List<RideDetail> rideDetailList = CloudConnect.callDropMeApi(false).getRideDetailList(idsList, geoPt).execute().getItems();
                        if(rideDetailList != null)
                        {
                            ConsoleLog.i(TAG, "list size is : " + rideDetailList.size());
                        }
                        else
                        {
                            ConsoleLog.i(TAG, "list is null");
                        }
                        return rideDetailList;
                    } catch (Exception e) {
                        ConsoleLog.e(e);
                    }

                    return null;
                }

                @Override
                public <T> void result(T t) {
                    List<RideDetail> rideDetailList = (List<RideDetail>) t;

                    if (getRideDetailListCallBack != null)
                    {
                        getRideDetailListCallBack.getRideDetailList(rideDetailList);
                    }
                }
            });
        }
        else
        {
            ConsoleLog.i(TAG, "id list is zero");
            if (getRideDetailListCallBack != null)
            {
                getRideDetailListCallBack.getRideDetailList(new ArrayList<RideDetail>());
            }
        }
    }

    public void getMyRideList(boolean isRefresh, final int skip, final int pageSize, final GetRideInfoListCallBack getRideInfoListCallBack)
    {
        if(isRefresh || (myRideInfoList.size() <= (skip*pageSize)))
        {
            callAsync(new AsyncCallBack() {
                @Override
                public List<MyRideInfo> async()
                {
                    try
                    {
                        return CloudConnect.callDropMeApi(false).getRideListOfUser(skip,pageSize).execute().getItems();
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
                    List<MyRideInfo> myRideInfos = (List<MyRideInfo>) t;
                    if(myRideInfos != null)
                    {
                        myRideInfoList.addAll(myRideInfos);
                    }

                    if(getRideInfoListCallBack != null)
                    {
                        getRideInfoListCallBack.myRideInfoList(myRideInfos, skip);
                    }
                }
            });
        }
        else
        {
            List<MyRideInfo> myRideInfos = CommonUtil.getSubList(skip,pageSize,myRideInfoList);

            if(getRideInfoListCallBack != null)
            {
                getRideInfoListCallBack.myRideInfoList(myRideInfos, skip);
            }
        }
    }

    public void clearData()
    {
        instance = null;
    }

    public interface GetRideDetailListCallBack
    {
        void getRideDetailList(List<RideDetail> rideDetailList);
    }

    public interface RideCreateCallBack
    {
        void rideCreated();
        void rideCreateFailed();
    }

    public interface RideDetailCallBack
    {
        void getRideDetail(RideDetail rideDetail);
    }

    public interface CancleRideCallBack
    {
        void cancel(boolean isSuccess);
    }

    public interface GetRideInfoListCallBack
    {
        void myRideInfoList(List<MyRideInfo> myRideInfoList, int skip);
    }
}
