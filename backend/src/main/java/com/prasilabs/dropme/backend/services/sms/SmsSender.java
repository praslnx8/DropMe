package com.prasilabs.dropme.backend.services.sms;

import com.prasilabs.apiRequest.ApiFetcher;
import com.prasilabs.apiRequest.ApiRequestType;
import com.prasilabs.apiRequest.ApiResult;
import com.prasilabs.dropme.backend.debug.ConsoleLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prasi on 27/6/16.
 */
public class SmsSender {
    private static final String MSG_91_API_KEY = "117654AectIgINBKj57710a35";
    private static final String SENDER_ID = "DROPME";
    private static final String trans_url = "https://control.msg91.com/api/sendhttp.php?authkey=" + MSG_91_API_KEY + "&route=4&sender=" + SENDER_ID;
    private static final String TAG = SmsSender.class.getSimpleName();

    public static boolean sendMsg91Sms(String message, String number) {
        List<String> stringList = new ArrayList<>();
        stringList.add(number);

        return sendMsg91Sms(message, stringList);
    }

    public static boolean sendMsg91Sms(String message, List<String> numbers) {
        boolean success = false;

        try {
            StringBuilder numberStr = new StringBuilder();
            for (int i = 0; i < numbers.size(); i++) {
                numberStr.append(numbers.get(i));
                if (i != numbers.size() - 1) {
                    numberStr.append(",");
                }
            }

            String url = trans_url + "&message=" + message + "&mobiles=" + numberStr.toString();

            ApiResult apiResult = ApiFetcher.makeStringRequest(url, ApiRequestType.GET);

            if (apiResult.isSuccess()) {
                String result = apiResult.getResult();
                ConsoleLog.i(TAG, "result is " + result);
                success = true;

                if (result.contains("false") || result.contains("failed")) {
                    success = false;
                    ConsoleLog.w(TAG, "result is " + apiResult.getResult());
                }
            } else {
                ConsoleLog.w(TAG, "send sms req failed " + apiResult.getError());
            }
        } catch (Exception e) {
            ConsoleLog.e(e);
        }

        return success;
    }
}
