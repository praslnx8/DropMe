package com.prasilabs.dropme.modules.home.presenters;

import android.content.Context;
import android.content.Intent;

import com.prasilabs.dropme.core.CorePresenter;

/**
 * Created by prasi on 27/5/16.
 */
public class HomePresenter extends CorePresenter
{

    public static HomePresenter newInstance()
    {
        return new HomePresenter();
    }

    @Override
    protected void broadCastRecieved(Context context, Intent intent) {

    }
}
