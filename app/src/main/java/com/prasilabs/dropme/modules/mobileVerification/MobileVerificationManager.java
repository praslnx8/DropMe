package com.prasilabs.dropme.modules.mobileVerification;

import android.content.Context;

import com.prasilabs.dropme.debug.ConsoleLog;

/**
 * Created by prasi on 27/5/16.
 */
public class MobileVerificationManager
{
    private static final String DIGIT_CONSUMER_KEY = "R3wUihoryBxcf9jtKqCzPS4X4";
    private static final String DIGIT_CONSUMER_SECRET_KEY = "abJ10Ar1xpMeirlK5BrvkRzIMsAryz2sHMEqI2DWPtLudInm7u";

    private static final String TAG = MobileVerificationManager.class.getSimpleName();

    public static void getVerifiedMobieNumber(Context context, final VerificationCallBack verificationCallBack)
    {
        ConsoleLog.i(TAG, "asking mobile verification");

        /*TwitterAuthConfig authConfig =  new TwitterAuthConfig(DIGIT_CONSUMER_KEY, DIGIT_CONSUMER_SECRET_KEY);
        Fabric.with(context, new TwitterCore(authConfig), new Digits());
        AuthCallback authCallback = new AuthCallback()
        {
            @Override
            public void success(DigitsSession session, String phoneNumber)
            {
                ConsoleLog.i(TAG, "digit succes returned");

                verificationCallBack.verify(true, session.getPhoneNumber());
            }

            @Override
            public void failure(DigitsException exception)
            {
                ConsoleLog.e(exception);
                ConsoleLog.w(TAG, "error code is : " + exception.getErrorCode());

                if(verificationCallBack != null)
                {
                    verificationCallBack.verify(false, null);
                }
            }
        };

        Digits.authenticate(authCallback, android.R.style.Theme_Material);*/
    }

    public interface VerificationCallBack
    {
        void verify(boolean status, String phone);
    }
}
