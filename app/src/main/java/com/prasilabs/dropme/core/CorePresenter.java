package com.prasilabs.dropme.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.prasilabs.dropme.debug.ConsoleLog;

/**
 * Created by prasi on 26/5/16.
 */
public abstract class CorePresenter
{
    private static final String TAG = CorePresenter.class.getSimpleName();
    private BroadcastReceiver broadcastReceiver;


    public void onCreate()
    {
        broadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                broadCastRecieved(context, intent);
            }
        };

        onCreateCalled();
    }

    protected abstract void onCreateCalled();

    protected void registerReciever(IntentFilter intentFilter)
    {
        if(CoreApp.getAppContext() != null)
        {
            LocalBroadcastManager.getInstance(CoreApp.getAppContext()).registerReceiver(broadcastReceiver, intentFilter);
        }
        else
        {
            ConsoleLog.w(TAG, "context is null :( core app");
        }
    }

    protected abstract void broadCastRecieved(Context context, Intent intent);

    public void onDestroy()
    {
        if(broadcastReceiver != null)
        {
            LocalBroadcastManager.getInstance(CoreApp.getAppContext()).unregisterReceiver(broadcastReceiver);
        }
    }
}
