package com.prasilabs.dropme.modules.splashLogin.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.prasilabs.dropme.constants.BroadCastConstant;
import com.prasilabs.util.ValidateUtil;
import com.prasilabs.dropme.backend.dropMeApi.model.VDropMeUser;
import com.prasilabs.dropme.core.CorePresenter;
import com.prasilabs.dropme.modelengines.DropMeUserModelEngine;

/**
 * Created by prasi on 26/5/16.
 */
public class SplashLoginPresenter extends CorePresenter
{
    private LoginCallBack loginCallBack;
    public SplashLoginPresenter(LoginCallBack loginCallBack)
    {
        this.loginCallBack = loginCallBack;
    }

    public static SplashLoginPresenter newInstance(final LoginCallBack loginCallBack)
    {
        return new SplashLoginPresenter(loginCallBack);
    }

    @Override
    protected void onCreateCalled()
    {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadCastConstant.LOCATION_ENABLED_CONSTANT);
        registerReciever(intentFilter);
    }

    @Override
    protected void broadCastRecieved(Context context, Intent intent)
    {
        if(intent.getAction().equals(BroadCastConstant.LOCATION_ENABLED_CONSTANT))
        {
            if(loginCallBack != null)
            {
                loginCallBack.locationEnabled();
            }
        }
    }

    public void login(VDropMeUser vDropMeUser)
    {
        if(validateDropMeUser(vDropMeUser))
        {
            DropMeUserModelEngine.getInstance().signup(vDropMeUser, new DropMeUserModelEngine.GetUserCallBack() {
                @Override
                public void getUser(VDropMeUser vDropMeUser)
                {
                    if(vDropMeUser != null && vDropMeUser.getId() != null && !TextUtils.isEmpty(vDropMeUser.getHash()))
                    {
                        loginCallBack.loginSuccess(vDropMeUser);
                    }
                    else
                    {
                        loginCallBack.Failed("Unable to Login");
                    }
                }
            });
        }
        else
        {
            loginCallBack.Failed("Login Failed");
        }

    }


    public interface LoginCallBack
    {
        void loginSuccess(VDropMeUser vDropMeUser);

        void Failed(String message);

        void locationEnabled();
    }

    private boolean validateDropMeUser(VDropMeUser vDropMeUser)
    {
        boolean isValid = false;

        if(vDropMeUser != null)
        {
            isValid = true;

            if(!ValidateUtil.validateEmail(vDropMeUser.getEmail()))
            {
                isValid = false;
            }
            else if(TextUtils.isEmpty(vDropMeUser.getLoginType()))
            {
                isValid = false;
            }
            else if(TextUtils.isEmpty(vDropMeUser.getName()))
            {
                isValid = false;
            }
            else if(vDropMeUser.getGender() == null)
            {
                isValid = false;
            }
        }

        return isValid;
    }
}
