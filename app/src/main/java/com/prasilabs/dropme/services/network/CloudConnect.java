package com.prasilabs.dropme.services.network;

import android.support.annotation.Nullable;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.prasilabs.dropme.backend.dropMeApi.DropMeApi;
import com.prasilabs.dropme.core.CoreApp;

/**
 * Created by kiran on 8/27/2015.
 * Access cloud end points from client libraries
 */

public class CloudConnect
{
    private static String serverUrl = "https://dropme.appspot.com/_ah/api/";
    private static String serverUrl_Dev = "https://localhost:8080/_ah/api/";

    public static DropMeApi callDropMeApi(@Nullable GoogleAccountCredential googleAccountCredential)
    {
        DropMeApi.Builder builder = new DropMeApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), googleAccountCredential);
        if(CoreApp.appDebug)
        {
            builder.setRootUrl(serverUrl_Dev);
        }
        else
        {
            builder.setRootUrl(serverUrl);
        }

        builder.setApplicationName(CoreApp.getAppContext().getPackageName());
        return builder.build();
    }
}