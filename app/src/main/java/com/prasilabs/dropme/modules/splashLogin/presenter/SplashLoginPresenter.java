package com.prasilabs.dropme.modules.splashLogin.presenter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.prasilabs.util.ValidateUtil;
import com.prasilabs.dropme.backend.dropMeApi.model.VDropMeUser;
import com.prasilabs.dropme.core.CorePresenter;
import com.prasilabs.dropme.modelengines.DropMeUserModelEngine;

/**
 * Created by prasi on 26/5/16.
 */
public class SplashLoginPresenter extends CorePresenter
{

    private SplashLoginPresenter(){}

    public static SplashLoginPresenter newInstance()
    {
        return new SplashLoginPresenter();
    }

    @Override
    protected void onCreateCalled()
    {

    }

    @Override
    protected void broadCastRecieved(Context context, Intent intent)
    {

    }

    public void login(VDropMeUser vDropMeUser, final LoginCallBack loginCallBack)
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
            else if(vDropMeUser.getGender() == 0)
            {
                isValid = false;
            }
        }

        return isValid;
    }
}
