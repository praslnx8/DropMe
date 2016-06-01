package com.prasilabs.dropme.modelengines;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.prasilabs.dropme.backend.dropMeApi.model.ApiResponse;
import com.prasilabs.dropme.backend.dropMeApi.model.RideDetail;
import com.prasilabs.dropme.backend.dropMeApi.model.RideInput;
import com.prasilabs.dropme.constants.BroadCastConstant;
import com.prasilabs.dropme.core.CoreApp;
import com.prasilabs.dropme.core.CoreModelEngine;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.managers.RideManager;
import com.prasilabs.dropme.managers.UserManager;
import com.prasilabs.dropme.services.network.CloudConnect;

import java.util.List;

/**
 * Created by prasi on 31/5/16.
 */
public class RideModelEngine extends CoreModelEngine
{
    private static RideModelEngine instance;

    public static RideModelEngine getInstance()
    {
        if(instance == null)
        {
            instance = new RideModelEngine();
        }

        return instance;
    }

    public void createRide(final RideInput rideInput, final RideCreateCallBack rideCreateCallBack)
    {
        callAsync(new AsyncCallBack()
        {
            @Override
            public RideInput async()
            {
                try
                {
                    RideInput output = CloudConnect.callDropMeApi(false).createRide(UserManager.getDropMeUser(CoreApp.getAppContext()).getHash(), rideInput).execute();
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

                RideManager.saveRideLite(CoreApp.getAppContext(), output);

                HomeGeoModelEngine.getInstance().addRidePoint(rideInput);

                Intent intent = new Intent();
                intent.setAction(BroadCastConstant.RIDE_REFRESH_INTENT);
                LocalBroadcastManager.getInstance(CoreApp.getAppContext()).sendBroadcast(intent);

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
                    RideInput rideInput = CloudConnect.callDropMeApi(false).getCurrentRide(UserManager.getDropMeUser(CoreApp.getAppContext()).getHash(), CoreApp.getDeviceId()).execute();
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
        callAsync(new AsyncCallBack() {
            @Override
            public RideDetail async()
            {
                try
                {
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


    public void cancelRide(final long rideId)
    {
        callAsync(new AsyncCallBack() {
            @Override
            public ApiResponse async()
            {
                try
                {
                    return CloudConnect.callDropMeApi(false).cancelRide(UserManager.getUserHash(CoreApp.getAppContext()), rideId).execute();
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
                if(apiResponse.getStatus())
                {

                }
                else
                {

                }
            }
        });
    }

    public void getRideDetailsList(final GetRideDetailListCallBack getRideDetailListCallBack)
    {
        final List<Long> idsList = HomeGeoModelEngine.getInstance().getAllRides();

        callAsync(new AsyncCallBack() {
            @Override
            public List<RideDetail> async()
            {
                try
                {
                    return CloudConnect.callDropMeApi(false).getRideDetailList(UserManager.getUserHash(CoreApp.getAppContext()), idsList).execute().getItems();
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
                List<RideDetail> rideDetailList = (List<RideDetail>) t;

                if(getRideDetailListCallBack != null)
                {
                    getRideDetailListCallBack.getRideDetailList(rideDetailList);
                }
            }
        });
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
}
