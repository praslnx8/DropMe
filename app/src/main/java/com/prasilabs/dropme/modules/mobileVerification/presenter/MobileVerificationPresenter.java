package com.prasilabs.dropme.modules.mobileVerification.presenter;

import android.content.Context;
import android.content.Intent;

import com.prasilabs.dropme.core.CorePresenter;

/**
 * Created by prasi on 30/6/16.
 */
public class MobileVerificationPresenter extends CorePresenter {

    @Override
    protected void onCreateCalled() {

    }

    @Override
    protected void broadCastRecieved(Context context, Intent intent) {

    }

    public void sendOtp(String phone) {

    }

    public void verifyOtp(String phone, String otp) {

    }

    public interface OtpVerifyCallBack {
        void verified(boolean status);

        void otpSent(boolean status);
    }
}
