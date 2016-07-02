package com.prasilabs.dropme.modelengines;

import com.prasilabs.dropme.backend.dropMeApi.model.ApiResponse;
import com.prasilabs.dropme.backend.dropMeApi.model.DropMeUserDetail;
import com.prasilabs.dropme.backend.dropMeApi.model.VDropMeUser;
import com.prasilabs.dropme.core.CoreApp;
import com.prasilabs.dropme.core.CoreModelEngine;
import com.prasilabs.dropme.customs.LocalPreference;
import com.prasilabs.dropme.managers.UserManager;
import com.prasilabs.dropme.services.network.CloudConnect;

/**
 * Created by prasi on 26/5/16.
 */
public class DropMeUserModelEngine extends CoreModelEngine
{
    private static DropMeUserModelEngine instance;

    public static DropMeUserModelEngine getInstance()
    {
        if(instance == null)
        {
            instance = new DropMeUserModelEngine();
        }

        return instance;
    }

    public void signup(final VDropMeUser vDropMeUser, final GetUserCallBack getUserCallBack)
    {

        callAsync(new AsyncCallBack() {
            @Override
            public VDropMeUser async() throws Exception
            {
                return CloudConnect.callDropMeApi(true).loginsignup(vDropMeUser).execute();
            }

            @Override
            public <T> void result(T t)
            {
                if(getUserCallBack != null)
                {
                    if(t != null)
                    {
                        getUserCallBack.getUser((VDropMeUser) t);
                    }
                    else
                    {
                        getUserCallBack.getUser(null);
                    }
                }
            }

            @Override
            public void error(int errorCode) {

            }
        });
    }

    public void getDropMeUser(final long id, final GetUserCallBack getUserCallBack)
    {
        callAsync(new AsyncCallBack() {
            @Override
            public VDropMeUser async() throws Exception
            {
                return CloudConnect.callDropMeApi(false).getUserDetail(id).execute();
            }

            @Override
            public <T> void result(T t)
            {
                if(getUserCallBack != null)
                {
                    getUserCallBack.getUser((VDropMeUser) t);
                }
            }

            @Override
            public void error(int errorCode) {

            }
        });
    }

    public void getDropMeUserDetail(final long userId, final GetUserDetailCallBack getUserDetailCallBack) {
        callAsync(new AsyncCallBack() {
            @Override
            public DropMeUserDetail async() throws Exception {
                return CloudConnect.callDropMeApi(false).getDropMeUserDetail(userId).execute();
            }

            @Override
            public <T> void result(T t) {
                DropMeUserDetail dropMeUserDetail = (DropMeUserDetail) t;

                if (getUserDetailCallBack != null) {
                    getUserDetailCallBack.getUser(dropMeUserDetail);
                }
            }

            @Override
            public void error(int errorCode) {

            }
        });
    }

    public void getLoginInfo(final GetLoginInfoCallBack getLoginInfoCallBack) {
        long lastLoginCheckTime = LocalPreference.getLoginDataFromShared(CoreApp.getAppContext(), "LAST_LOGIN_CHECK_TIME", 0L);

        final VDropMeUser vDropMeUser = UserManager.getDropMeUser(CoreApp.getAppContext());

        if ((System.currentTimeMillis() - lastLoginCheckTime > 8 * 60 * 60) || vDropMeUser == null) {
            callAsync(new AsyncCallBack() {
                @Override
                public VDropMeUser async() throws Exception {
                    return CloudConnect.callDropMeApi(false).getLoginInfo().execute();
                }

                @Override
                public <T> void result(T t) {
                    VDropMeUser loginInfo = (VDropMeUser) t;
                    if (loginInfo == null) {
                        LocalPreference.clearLoginSharedPreferences(CoreApp.getAppContext());
                    } else {
                        UserManager.saveDropMeUser(CoreApp.getAppContext(), loginInfo);
                    }

                    if (getLoginInfoCallBack != null) {
                        getLoginInfoCallBack.getLoginInfo(loginInfo);
                    }
                }

                @Override
                public void error(int errorCode) {
                    //do nothing
                }
            }, true);
        } else {
            if (getLoginInfoCallBack != null) {
                getLoginInfoCallBack.getLoginInfo(vDropMeUser);
            }
        }
    }

    public void sendOtp(final String phone, final OtpSendCallBack otpCallBack) {
        callAsync(new AsyncCallBack() {
            @Override
            public ApiResponse async() throws Exception {
                return CloudConnect.callDropMeApi(false).sendOtp(phone).execute();
            }

            @Override
            public <T> void result(T t) {
                ApiResponse apiResponse = (ApiResponse) t;

                if (otpCallBack != null) {
                    if (apiResponse != null && apiResponse.getStatus()) {
                        otpCallBack.otpSent(true);
                    } else {
                        otpCallBack.otpSent(false);
                    }
                }
            }

            @Override
            public void error(int errorCode) {

            }
        });
    }

    public void verifyOtp(final String otp, final OtpVerifyCallBack otpVerifyCallBack) {
        callAsync(new AsyncCallBack() {
            @Override
            public ApiResponse async() throws Exception {
                return CloudConnect.callDropMeApi(false).verifyOtp(otp).execute();
            }

            @Override
            public <T> void result(T t) {
                ApiResponse apiResponse = (ApiResponse) t;

                if (otpVerifyCallBack != null) {
                    if (apiResponse != null && apiResponse.getStatus()) {
                        otpVerifyCallBack.otpVerified(true);
                    } else {
                        otpVerifyCallBack.otpVerified(false);
                    }
                }
            }

            @Override
            public void error(int errorCode) {

            }
        });
    }

    public interface GetUserCallBack
    {
        void getUser(VDropMeUser vDropMeUser);
    }

    public interface GetLoginInfoCallBack {
        void getLoginInfo(VDropMeUser vDropMeUser);
    }

    public interface OtpSendCallBack {
        void otpSent(boolean status);
    }

    public interface OtpVerifyCallBack {
        void otpVerified(boolean status);
    }

    public interface GetUserDetailCallBack {
        void getUser(DropMeUserDetail dropMeUserDetail);
    }
}
