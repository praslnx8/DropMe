package com.prasilabs.dropme.modelengines;

import android.os.Build;
import android.text.TextUtils;

import com.prasilabs.constants.CommonConstant;
import com.prasilabs.dropme.backend.dropMeApi.model.ApiResponse;
import com.prasilabs.dropme.backend.dropMeApi.model.CrashReportIO;
import com.prasilabs.dropme.core.CoreApp;
import com.prasilabs.dropme.core.CoreModelEngine;
import com.prasilabs.dropme.customs.LocalPreference;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.managers.UserManager;
import com.prasilabs.dropme.services.network.CloudConnect;
import com.prasilabs.enums.CrashType;

/**
 * Created by prasi on 23/6/16.
 */
public class CrashReportModelEngine extends CoreModelEngine {
    private static final String TAG = CrashReportModelEngine.class.getSimpleName();
    private static CrashReportModelEngine instance;

    private CrashReportModelEngine() {
    }

    public static CrashReportModelEngine getInstance() {
        if (instance == null) {
            instance = new CrashReportModelEngine();
        }

        return instance;
    }

    public void reportCrash(String stackTrace, CrashType crashType) {
        final CrashReportIO crashReportIO = new CrashReportIO();
        crashReportIO.setEmail(UserManager.getUserEmail(CoreApp.getAppContext()));
        crashReportIO.setAndroidVersion(Build.VERSION.CODENAME);
        crashReportIO.setCrashMessage(stackTrace);
        crashReportIO.setDeviceName(Build.MANUFACTURER);
        crashReportIO.setModelName(Build.MODEL);
        crashReportIO.setDeviceId(CoreApp.getDeviceId());
        crashReportIO.setCrashType(crashType.name());

        callAsync(new AsyncCallBack() {
            @Override
            public ApiResponse async() {
                try {
                    return CloudConnect.callDropMeApi(false).sendCrashReport(crashReportIO).execute();
                } catch (Exception e) {
                    ConsoleLog.w(TAG, "error sending crash report");
                }
                return null;
            }

            @Override
            public <T> void result(T t) {
                ApiResponse apiResponse = (ApiResponse) t;

                if (apiResponse != null) {
                    if (apiResponse.getStatus()) {
                        LocalPreference.saveAppDataInShared(CoreApp.getAppContext(), CommonConstant.CRASH_STACKTRACE_STR, "");
                    }
                }
            }
        });

    }

    public void reportCrashIfExist() {
        String stackTrace = LocalPreference.getAppDataFromShared(CoreApp.getAppContext(), CommonConstant.CRASH_STACKTRACE_STR, null);

        if (!TextUtils.isEmpty(stackTrace)) {
            reportCrash(stackTrace, CrashType.EXCEPTION);
        }
    }

    public interface CrashReportCallBack {
        void result(boolean success);
    }
}
