package com.prasilabs.dropme.services.oauth;

import android.content.Context;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.prasilabs.constants.AuthConstants;
import com.prasilabs.dropme.constants.PojoConstants;
import com.prasilabs.dropme.customs.LocalPreference;

/**
 * Created by prasi on 26/5/16.
 */
public class GCredential
{
    private static String prevEmail;
    private static GoogleAccountCredential googleAccountCredential;

    public static GoogleAccountCredential getGoogleCredential(Context context)
    {
        String email = LocalPreference.getLoginDataFromShared(context, PojoConstants.UserConstant.EMAIL_STR, null);
        if(email != null)
        {
            if (googleAccountCredential == null || prevEmail == null || !email.equals(prevEmail))
            {
                googleAccountCredential = GoogleAccountCredential.usingAudience(context, AuthConstants.AUDIENCE_PRE_TEXT + AuthConstants.WEB_CLIENT_ID);
                googleAccountCredential.setSelectedAccountName(email);
                prevEmail = email;
            }
        }
        return googleAccountCredential;
    }
}
