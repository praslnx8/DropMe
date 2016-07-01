package com.prasilabs.dropme.core;

import android.os.AsyncTask;

import com.prasilabs.dropme.constants.ErrorCodes;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.services.network.NetworkManager;
import com.prasilabs.dropme.utils.ViewUtil;

import java.io.IOException;

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

            if (asyncCallBack != null) {
                asyncCallBack.error(ErrorCodes.NOT_CONNECTED);
            }
        }
    }

    private <T> void call(final AsyncCallBack asyncCallBack) {
        new AsyncTask<Void, Void, T>()
        {
            private int errorCode = 0;

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
            }

            @Override
            protected T doInBackground(Void... params)
            {
                try
                {
                    if (asyncCallBack != null) {
                        return asyncCallBack.async();
                    }
                } catch (Exception e) {
                    ConsoleLog.e(e);

                    if (e instanceof IOException) {
                        errorCode = ErrorCodes.TIME_OUT;
                    } else {
                        errorCode = ErrorCodes.GENERAL;
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(T t)
            {
                super.onPostExecute(t);

                if(asyncCallBack != null)
                {
                    if (errorCode > 0) {
                        asyncCallBack.error(errorCode);
                    } else {
                        asyncCallBack.result(t);
                    }
                }
            }
        }.execute();
    }

    public interface AsyncCallBack
    {
        <T> T async() throws Exception;

        <T> void result(T t);

        void error(int errorCode);
    }
}
