package com.prasilabs.dropme.modelengines;

import com.prasilabs.dropme.backend.dropMeApi.model.VDropMeUser;
import com.prasilabs.dropme.core.CoreModelEngine;
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
            public VDropMeUser asyncc() throws Exception
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
            public VDropMeUser asyncc() throws Exception
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

    public interface GetUserCallBack
    {
        void getUser(VDropMeUser vDropMeUser);
    }


}
