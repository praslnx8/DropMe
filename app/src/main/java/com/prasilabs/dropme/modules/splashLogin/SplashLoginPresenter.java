package com.prasilabs.dropme.modules.splashLogin;

import android.content.Context;
import android.content.Intent;

import com.prasilabs.dropme.core.CorePresenter;
import com.prasilabs.dropme.datamodels.CDropMeUser;

/**
 * Created by prasi on 26/5/16.
 */
public class SplashLoginPresenter extends CorePresenter
{

    @Override
    protected void broadCastRecieved(Context context, Intent intent)
    {

    }

    public interface LoginCallBack
    {
        void loginSuccess(CDropMeUser cDropMeUser);

        void Failed();

        void signupSuccess(long id);
    }
}
