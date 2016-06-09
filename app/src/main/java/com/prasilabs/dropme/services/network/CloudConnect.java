package com.prasilabs.dropme.services.network;

import android.text.TextUtils;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.prasilabs.dropme.backend.dropMeApi.DropMeApi;
import com.prasilabs.dropme.constants.PojoConstants;
import com.prasilabs.dropme.core.CoreApp;
import com.prasilabs.dropme.customs.LocalPreference;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.services.oauth.DropMeCredential;
import com.prasilabs.dropme.services.oauth.FCredential;
import com.prasilabs.dropme.services.oauth.GCredential;
import com.prasilabs.enums.LoginType;

/**
 * Created by kiran on 8/27/2015.
 * Access cloud end points from client libraries
 */

public class CloudConnect
{
    private static final String TAG = CloudConnect.class.getSimpleName();
    private static DropMeApi dropMeApi;
    private static String serverUrl = "https://prasilabs-dropme.appspot.com/_ah/api/";
    private static String serverUrl_Dev = "https://prasilabs-dropme.appspot.com/_ah/api/"; //"http://10.0.0.12:8080/_ah/api/";
    private static boolean isLastCallOauth;

    public static DropMeApi callDropMeApi(boolean isOauth)
    {
        if(isOauth || dropMeApi == null || isLastCallOauth)
        {
            String loginTypeStr = LocalPreference.getLoginDataFromShared(CoreApp.getAppContext(), PojoConstants.UserConstant.LOGIN_TYPE_STR, null);
            String accesToken = LocalPreference.getLoginDataFromShared(CoreApp.getAppContext(), PojoConstants.UserConstant.ACCES_TOKEN_STR, null);
            String email = LocalPreference.getLoginDataFromShared(CoreApp.getAppContext(), PojoConstants.UserConstant.EMAIL_STR, null);

            HttpRequestInitializer httpRequestInitializer = null;

            if(isOauth)
            {
                if (!TextUtils.isEmpty(loginTypeStr) && loginTypeStr.equals(LoginType.FaceBook.name()) && !TextUtils.isEmpty(accesToken))
                {
                    ConsoleLog.i(TAG, "facebook oauth");
                    httpRequestInitializer = FCredential.getFbAccountCredential(accesToken);
                }
                else if (!TextUtils.isEmpty(loginTypeStr) && loginTypeStr.equals(LoginType.GPlus.name()) && !TextUtils.isEmpty(email))
                {
                    ConsoleLog.i(TAG, "google oauth");
                    httpRequestInitializer = GCredential.getGoogleCredential(CoreApp.getAppContext());
                }
                isLastCallOauth = true;
            }
            else
            {
                httpRequestInitializer = DropMeCredential.getDropMECredentialInitializer();
                isLastCallOauth = false;
            }

            DropMeApi.Builder builder = new DropMeApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), httpRequestInitializer);
            if (CoreApp.appDebug) {
                builder.setRootUrl(serverUrl_Dev);
            } else {
                builder.setRootUrl(serverUrl);
            }

            builder.setApplicationName(CoreApp.getAppContext().getPackageName());

            dropMeApi = builder.build();
        }
        return dropMeApi;
    }
}