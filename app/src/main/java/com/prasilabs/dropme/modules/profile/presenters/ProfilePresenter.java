package com.prasilabs.dropme.modules.profile.presenters;

import android.content.Context;
import android.content.Intent;

import com.prasilabs.dropme.backend.dropMeApi.model.DropMeUserDetail;
import com.prasilabs.dropme.core.CorePresenter;
import com.prasilabs.dropme.modelengines.DropMeUserModelEngine;

/**
 * Created by prasi on 5/6/16.
 */
public class ProfilePresenter extends CorePresenter
{

    @Override
    protected void onCreateCalled()
    {

    }

    @Override
    protected void broadCastRecieved(Context context, Intent intent)
    {

    }

    public void getProfile(long userID, final GetProfileCallBack getProfileCallBack)
    {
        DropMeUserModelEngine.getInstance().getDropMeUserDetail(userID, new DropMeUserModelEngine.GetUserDetailCallBack() {
            @Override
            public void getUser(DropMeUserDetail dropMeUserDetail) {
                if (getProfileCallBack != null) {
                    getProfileCallBack.getUserDetail(dropMeUserDetail);
                }
            }
        });
    }

    public interface GetProfileCallBack {
        void getUserDetail(DropMeUserDetail dropMeUserDetail);
    }
}
