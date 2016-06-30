package com.prasilabs.dropme.backend.utils;

import com.prasilabs.dropme.backend.services.sms.SmsSender;

/**
 * Created by prasi on 30/6/16.
 */
public class SmsSenderUtil {
    public static boolean sendOtpSms(String otp, String name, String phone) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Your OTP is " + otp);
        stringBuilder.append(" ,");
        stringBuilder.append("Hi " + name + " Welcome to DROPME");

        return SmsSender.sendMsg91Sms(stringBuilder.toString(), phone);
    }
}
