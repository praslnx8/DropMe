package com.prasilabs.dropme.backend.services.gcm;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.prasilabs.constants.GcmConstants;
import com.prasilabs.dropme.backend.debug.ConsoleLog;
import com.prasilabs.dropme.backend.logicEngines.GcmLogicEngine;

import java.io.IOException;
import java.util.List;

/**
 * Created by prasi on 5/6/16.
 */
public class GcmSender
{
    private static final String TAG = GcmSender.class.getSimpleName();

    private static final String API_KEY = "AIzaSyDo3LXdo7rZxENytQeq0HWM1yfupmNNOxU";//System.getProperty("gcm.api.key");
    private static Sender sender;

    public static boolean sendGcmMessage(long id, String jobType, String msg, String gcmId)
    {
        ConsoleLog.i(TAG, "sending gcm");
        boolean success = false;

        if(sender == null)
        {
            sender = new Sender(API_KEY);
        }

        Message message = new Message.Builder().addData(GcmConstants.ID_KEY, String.valueOf(id)).addData(GcmConstants.JOB_TYPE_KEY, jobType).addData(GcmConstants.MESSAGE_KEY, msg).build();

        try
        {
            Result result = sender.send(message, gcmId, 2);

            if (result.getMessageId() != null)
            {
                ConsoleLog.i(TAG, "message sent to " + gcmId);
                String canonicalId = result.getCanonicalRegistrationId();
                if (canonicalId != null)
                {
                    ConsoleLog.i(TAG, "gcmID changed");
                    GcmLogicEngine.getInstance().updateGcmRecord(gcmId, canonicalId);
                }

                success = true;
            }
            else
            {
                String error = result.getErrorCodeName();
                if (error.equals(Constants.ERROR_NOT_REGISTERED))
                {
                    ConsoleLog.w(TAG, " registration id " + gcmId + " is invalid");
                    GcmLogicEngine.getInstance().deleteGcmRecord(gcmId);
                }
                else
                {
                    ConsoleLog.w(TAG, "Error sending gcm message to " + gcmId);
                }
            }
        } catch (Exception e) {
            ConsoleLog.e(e);
        }

        ConsoleLog.i(TAG, "gcmm sent with status " + success);


        return success;
    }

    public static boolean sendGcmMessage(long id, String msg, String jobType, List<String> gcmIDs)
    {
        boolean isSuccess = false;

        if (gcmIDs != null && gcmIDs.size() > 0)
        {
            if (sender == null) {
                sender = new Sender(API_KEY);
            }

            Message message = new Message.Builder().addData(GcmConstants.ID_KEY, String.valueOf(id)).addData(GcmConstants.JOB_TYPE_KEY, jobType).addData(GcmConstants.MESSAGE_KEY, msg).build();

            try {
                MulticastResult multicastResult = sender.send(message, gcmIDs, 2);

                for (String string : gcmIDs) {
                    ConsoleLog.i(TAG, "gcm id is : " + string);
                }

                ConsoleLog.i(TAG, "total gcm sent is : " + multicastResult.getTotal());
                ConsoleLog.i(TAG, "total gcm success is : " + multicastResult.getSuccess());
                if (multicastResult.getFailure() > 0) {
                    ConsoleLog.w(TAG, "total gcm failure is : " + multicastResult.getFailure());
                }
                if (multicastResult.getCanonicalIds() > 0) {
                    ConsoleLog.w(TAG, "total gcm canonicals is : " + multicastResult.getCanonicalIds());
                }

                if (multicastResult.getSuccess() > 0) {
                    isSuccess = true;
                }
            } catch (IOException e) {
                ConsoleLog.e(e);
            }
        }

        return isSuccess;
    }
}
