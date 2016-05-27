package com.prasilabs.dropme.services.oauth;

import android.content.Context;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.prasilabs.AuthConstants;
import com.prasilabs.dropme.constants.UserConstant;
import com.prasilabs.dropme.customs.LocalPreference;

/**
 * Created by prasi on 26/5/16.
 */
public class GCredential
{
    private static GoogleAccountCredential googleAccountCredential;

    public static GoogleAccountCredential getGoogleCredential(Context context)
    {
        if(googleAccountCredential == null)
        {
            googleAccountCredential = GoogleAccountCredential.usingAudience(context, AuthConstants.AUDIENCE_PRE_TEXT + AuthConstants.WEB_CLIENT_ID);
            googleAccountCredential.setSelectedAccountName(LocalPreference.getLoginDataFromShared(context, UserConstant.EMAIL_STR, null));
        }
        return googleAccountCredential;
    }

    public static void clearCredentialOnLogout()
    {
        googleAccountCredential = null;
    }
}
