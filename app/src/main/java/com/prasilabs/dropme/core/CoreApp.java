package com.prasilabs.dropme.core;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.prasilabs.dropme.BuildConfig;

/**
 * CoreApp. The starting point of android app
 */
public class CoreApp extends Application
{
    public static final String TAG = CoreApp.class.getSimpleName();

    private static CoreApp mInstance;
    public static boolean appDebug = BuildConfig.DEBUG;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mInstance = this;
    }

    public static Context getAppContext()
    {
        return mInstance.getApplicationContext();
    }

    public static synchronized CoreApp getInstance() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}