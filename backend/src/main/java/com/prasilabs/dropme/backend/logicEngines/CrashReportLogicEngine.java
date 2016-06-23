package com.prasilabs.dropme.backend.logicEngines;

import com.googlecode.objectify.Key;
import com.prasilabs.dropme.backend.core.CoreLogicEngine;
import com.prasilabs.dropme.backend.datastore.CrashReport;
import com.prasilabs.dropme.backend.db.OfyService;
import com.prasilabs.dropme.backend.debug.ConsoleLog;
import com.prasilabs.dropme.backend.io.ApiResponse;
import com.prasilabs.dropme.backend.io.CrashReportIO;
import com.prasilabs.util.ValidateUtil;

import java.util.Date;

/**
 * Created by prasi on 23/6/16.
 */
public class CrashReportLogicEngine extends CoreLogicEngine {
    private static final String TAG = CrashReportLogicEngine.class.getSimpleName();
    private static CrashReportLogicEngine instance;

    private CrashReportLogicEngine() {
    }

    public static CrashReportLogicEngine getInstance() {
        if (instance == null) {
            instance = new CrashReportLogicEngine();
        }

        return instance;
    }

    private static CrashReport convertToCrashReport(CrashReportIO crashReportIO) {
        if (crashReportIO != null) {
            boolean isValid = true;
            if (ValidateUtil.isStringEmpty(crashReportIO.getCrashMessage())) {
                isValid = false;
            }

            if (isValid) {
                CrashReport crashReport = new CrashReport();

                crashReport.setEmail(crashReportIO.getEmail());
                crashReport.setDeviceId(crashReportIO.getDeviceId());
                crashReport.setAndroidVersion(crashReportIO.getAndroidVersion());
                crashReport.setCrashType(crashReportIO.getCrashType());
                crashReport.setCrashMessage(crashReportIO.getCrashMessage());
                crashReport.setModelName(crashReportIO.getModelName());
                crashReport.setDeviceName(crashReportIO.getDeviceName());
                crashReport.setCreated(crashReportIO.getCreated());

                return crashReport;
            }
        }

        return null;
    }

    public ApiResponse addCrashReport(CrashReportIO crashReportIO) {
        ApiResponse apiResponse = new ApiResponse();

        CrashReport crashReport = convertToCrashReport(crashReportIO);

        if (crashReport != null) {
            crashReport.setModified(new Date(System.currentTimeMillis()));

            Key<CrashReport> crashReportKey = OfyService.ofy().save().entity(crashReport).now();
            apiResponse.setStatus(true);
            apiResponse.setId(crashReportKey.getId());
        } else {
            ConsoleLog.w(TAG, "crash report is invalid");
        }


        return apiResponse;
    }
}
