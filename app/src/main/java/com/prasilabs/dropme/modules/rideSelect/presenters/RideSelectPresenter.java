package com.prasilabs.dropme.modules.rideSelect.presenters;

import android.content.Context;
import android.content.Intent;

import com.prasilabs.dropme.core.CorePresenter;

/**
 * Created by prasi on 30/5/16.
 */
public class RideSelectPresenter extends CorePresenter
{

    public static RideSelectPresenter newInstance()
    {
        return new RideSelectPresenter();
    }

    @Override
    protected void onCreateCalled() {

    }

    @Override
    protected void broadCastRecieved(Context context, Intent intent) {

    }
}
