package com.prasilabs.dropme.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by prasi on 26/5/16.
 */
public abstract class CorePresenter
{
    private BroadcastReceiver broadcastReceiver;


    public void onCreate(Context context)
    {
        broadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                broadCastRecieved(context, intent);
            }
        };
    }

    protected void registerReciever(IntentFilter intentFilter)
    {
        LocalBroadcastManager.getInstance(CoreApp.getAppContext()).registerReceiver(broadcastReceiver, intentFilter);
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
