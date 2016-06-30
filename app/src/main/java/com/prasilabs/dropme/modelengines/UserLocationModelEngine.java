package com.prasilabs.dropme.modelengines;

import com.prasilabs.dropme.backend.dropMeApi.model.ApiResponse;
import com.prasilabs.dropme.backend.dropMeApi.model.GeoPt;
import com.prasilabs.dropme.core.CoreApp;
import com.prasilabs.dropme.core.CoreModelEngine;
import com.prasilabs.dropme.managers.UserManager;
import com.prasilabs.dropme.services.network.CloudConnect;

/**
 * Created by prasi on 9/6/16.
 */
public class UserLocationModelEngine extends CoreModelEngine
{
    private static UserLocationModelEngine instance;

    private UserLocationModelEngine(){}

    public static UserLocationModelEngine getInstance()
    {
        if(instance == null)
        {
            instance = new UserLocationModelEngine();
        }

        return instance;
    }

    public void updateUserLocation(final String deviceId, final GeoPt geoPt, final UpdateUserLocCallBack userLocCallBack)
    {
        if (UserManager.isUserLoggedIn(CoreApp.getAppContext())) {
            callAsync(new AsyncCallBack()
            {
                @Override
                public ApiResponse async() throws Exception {
                    return CloudConnect.callDropMeApi(false).updateUserLocation(deviceId, geoPt).execute();
                }

                @Override
                public <T> void result(T t) {
                    ApiResponse apiResponse = (ApiResponse) t;

                    if (userLocCallBack != null)
                    {
                        if (apiResponse != null && apiResponse.getStatus() != null && apiResponse.getStatus()) {
                            userLocCallBack.locationUpdated(true);
                        } else {
                            userLocCallBack.locationUpdated(false);
                        }
                    }
                }
            }, true);
        }
    }


    public void shareLocation(final long recieverId, final ShareLocationCallBack shareLocationCallBack) {
        callAsync(new AsyncCallBack() {
            @Override
            public ApiResponse async() throws Exception {
                return CloudConnect.callDropMeApi(false).shareLocation(recieverId, CoreApp.getDeviceId()).execute();
            }

            @Override
            public <T> void result(T t) {
                ApiResponse apiResponse = (ApiResponse) t;

                if (shareLocationCallBack != null) {
                    shareLocationCallBack.locationShared();
                }
            }
        });
    }

    public interface UpdateUserLocCallBack
    {
        void locationUpdated(boolean success);
    }

    public interface ShareLocationCallBack {
        void locationShared();
    }
}
