package com.prasilabs.dropme.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class RideService extends Service
{
    public RideService()
    {

    }

    private static RideService self;

    @Override
    public void onCreate()
    {
        super.onCreate();
        self = this;
    }

    public static boolean stopService()
    {
        if(self != null)
        {
            self.stopSelf();
            return true;
        }

        return false;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
