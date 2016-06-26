package com.prasilabs.dropme.core;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.prasilabs.dropme.customs.LocalPreference;
import com.prasilabs.dropme.debug.ConsoleLog;
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

            if (asyncCallBack != null) {
                asyncCallBack.result(null);
            }
        }
    }

    private <T> void call(final AsyncCallBack asyncCallBack) {
        new AsyncTask<Void, Void, T>()
        {
            private boolean isOauthException = false;

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
                        return asyncCallBack.asyncc();
                    }
                } catch (Exception e) {
                    ConsoleLog.e(e);

                    String message = e.getMessage();
                    if (message != null && message.contains("Unauthorized")) {
                        isOauthException = true;
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
                    asyncCallBack.result(t);
                }

                if (isOauthException) {
                    Context context = CoreApp.getAppContext();
                    if (context != null) {
                        LocalPreference.clearLoginSharedPreferences(context);
                        Intent i = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                    }
                }
            }
        }.execute();
    }

    public interface AsyncCallBack
    {
        <T> T asyncc() throws Exception;

        <T> void result(T t);
    }
}
