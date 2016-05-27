package com.prasilabs.dropme.modelengines;

import com.prasilabs.dropme.backend.dropMeApi.model.VDropMeUser;
import com.prasilabs.dropme.core.CoreModelEngine;
import com.prasilabs.dropme.debug.ConsoleLog;
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

    public void signup(final VDropMeUser vDropMeUser, final LoginModelCallBack loginModelCallBack)
    {

        callAsync(new AsyncCallBack() {
            @Override
            public VDropMeUser async()
            {
                try
                {
                    return CloudConnect.callDropMeApi(true).loginsignup(vDropMeUser).execute();
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
                if(loginModelCallBack != null)
                {
                    if(t != null)
                    {
                        loginModelCallBack.login((VDropMeUser) t);
                    }
                    else
                    {
                        loginModelCallBack.login(null);
                    }
                }
            }
        });
    }

    public interface LoginModelCallBack
    {
        void login(VDropMeUser vDropMeUser);
    }
}
