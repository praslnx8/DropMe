package com.prasilabs.dropme.core;

import android.os.AsyncTask;

/**
 * Created by prasi on 27/5/16.
 */
public abstract class CoreModelEngine
{
    protected <T> void callAsync(final AsyncCallBack asyncCallBack)
    {
        new AsyncTask<Void, Void, T>()
        {
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
