package com.prasilabs.dropme.core;

import android.os.AsyncTask;

import com.prasilabs.dropme.services.network.NetworkManager;
import com.prasilabs.dropme.utils.ViewUtil;

/**
 * Created by prasi on 27/5/16.
 */
public abstract class CoreModelEngine
{
    protected <T> void callAsync(final AsyncCallBack asyncCallBack)
    {
        callAsync(asyncCallBack, false);
    }

    protected <T> void callAsync(final AsyncCallBack asyncCallBack, final boolean isBackgroundCall) {
        boolean isOnline = new NetworkManager(CoreApp.getAppContext(), /*new NetworkManager.NetworkHandler() {
            @Override
            public void onNetworkUpdate(boolean isOnline)
            {
                if(isBackgroundCall)
                {
                    call(asyncCallBack);
                }
            }
        }*/ null).isOnline();

        if (isOnline) {
            call(asyncCallBack);
        } else if (!isBackgroundCall) {
            ViewUtil.t(CoreApp.getAppContext(), "Please check the network and try again");
        }
    }

    private <T> void call(final AsyncCallBack asyncCallBack) {
        new AsyncTask<Void, Void, T>()
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
            }

            @Override
            protected T doInBackground(Void... params)
            {
                if(asyncCallBack != null)
                {
                    return asyncCallBack.async();
                }

                return null;
            }

            @Override
            protected void onPostExecute(T t)
            {
                super.onPostExecute(t);

                if(asyncCallBack != null)
                {
                    asyncCallBack.result(t);
                }
            }
        }.execute();
    }

    public interface AsyncCallBack
    {
        <T> T async();

        <T> void result(T t);
    }
}
