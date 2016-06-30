package com.prasilabs.dropme.backend.services.encryption;

import com.prasilabs.util.CommonUtil;

/**
 * Created by prasi on 30/6/16.
 */
public class OTPManager {
    public static String getOtp() {
        String randomNo = String.valueOf(CommonUtil.getRandomNumber(1000, 9999));

        return randomNo;
    }
}
