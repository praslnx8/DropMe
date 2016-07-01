package com.prasilabs.dropme.services.sms;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;

import com.prasilabs.constants.CommonConstant;
import com.prasilabs.dropme.constants.BroadCastConstant;
import com.prasilabs.dropme.core.CoreActivity;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.util.DataUtil;

/**
 * Created by prasi on 9/10/15.
 */
public class SmsVerificationReciever extends BroadcastReceiver {
    private static final String TAG = SmsVerificationReciever.class.getSimpleName();

    private static final SmsVerificationReciever smsVerificationReciever = new SmsVerificationReciever();

    public static void setSmsVerificationReciever(CoreActivity coreActivity) {
        if (ActivityCompat.checkSelfPermission(coreActivity, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.provider.Telephony.SMS_RECEIVED");
            coreActivity.registerReceiver(smsVerificationReciever, filter);
        }
    }

    public static void unRegisterSmsVerificationReciever(CoreActivity coreActivity) {
        try {
            if (smsVerificationReciever != null) {
                coreActivity.unregisterReceiver(smsVerificationReciever);
            }
        } catch (IllegalArgumentException e) {
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            ConsoleLog.i(TAG, "SMS RECIEVED");
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs;
            String msg_from;
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();

                        ConsoleLog.i(TAG, "message is :" + msgBody + " from :" + msg_from);
                        String[] messageStr = msgBody.split(" ");
                        if (messageStr.length > 4) {
                            String otp = messageStr[3];

                            ConsoleLog.i(TAG, "otp recieved as string is " + otp);
                            int otpAsInt = DataUtil.stringToInt(otp);
                            if (otpAsInt > 0) {
                                Intent smsRecievedIntent = new Intent();
                                smsRecievedIntent.putExtra(CommonConstant.OTP_STR, otp);
                                smsRecievedIntent.setAction(BroadCastConstant.SMS_RECIEVED_INTENT);

                                LocalBroadcastManager.getInstance(context).sendBroadcast(smsRecievedIntent);
                            }
                        }
                    }
                } catch (Exception e) {
                    ConsoleLog.e(e);
                }
            }
        }
    }
}