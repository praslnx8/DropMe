package com.prasilabs.dropme.modules.mobileVerification.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.prasilabs.constants.CommonConstant;
import com.prasilabs.dropme.constants.BroadCastConstant;
import com.prasilabs.dropme.core.CorePresenter;
import com.prasilabs.dropme.modelengines.DropMeUserModelEngine;

/**
 * Created by prasi on 30/6/16.
 */
public class MobileVerificationPresenter extends CorePresenter {
    private OtpVerifyCallBack otpVerifyCallBack;

    public MobileVerificationPresenter(OtpVerifyCallBack otpVerifyCallBack) {
        this.otpVerifyCallBack = otpVerifyCallBack;
    }

    @Override
    protected void onCreateCalled() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadCastConstant.SMS_RECIEVED_INTENT);
        registerReciever(intentFilter);
    }

    @Override
    protected void broadCastRecieved(Context context, Intent intent) {
        if (intent.getAction().equals(BroadCastConstant.SMS_RECIEVED_INTENT)) {
            String otp = intent.getStringExtra(CommonConstant.OTP_STR);

            if (!TextUtils.isEmpty(otp)) {
                if (otpVerifyCallBack != null) {
                    otpVerifyCallBack.otpRecieved(otp);
                }
            }
        }
    }

    public void sendOtp(String phone) {
        DropMeUserModelEngine.getInstance().sendOtp(phone, new DropMeUserModelEngine.OtpSendCallBack() {
            @Override
            public void otpSent(boolean status) {
                if (otpVerifyCallBack != null) {
                    otpVerifyCallBack.otpSent(status);
                }
            }
        });
    }

    public void verifyOtp(String otp) {
        DropMeUserModelEngine.getInstance().verifyOtp(otp, new DropMeUserModelEngine.OtpVerifyCallBack() {
            @Override
            public void otpVerified(boolean status) {
                if (otpVerifyCallBack != null) {
                    otpVerifyCallBack.verified(status);
                }
            }
        });
    }

    public interface OtpVerifyCallBack {
        void verified(boolean status);

        void otpSent(boolean status);

        void otpRecieved(String otp);
    }
}
