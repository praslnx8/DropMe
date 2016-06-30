package com.prasilabs.dropme.modelengines;

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
            });
        } else {
            if (getLoginInfoCallBack != null) {
                getLoginInfoCallBack.getLoginInfo(vDropMeUser);
            }
        }
    }

    public interface GetUserCallBack
    {
        void getUser(VDropMeUser vDropMeUser);
    }

    public interface GetLoginInfoCallBack {
        void getLoginInfo(VDropMeUser vDropMeUser);
    }
}
