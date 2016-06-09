package com.prasilabs.dropme.backend.services.gcm;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.prasilabs.constants.GcmConstants;
import com.prasilabs.dropme.backend.datastore.GcmRecord;
import com.prasilabs.dropme.backend.db.OfyService;
import com.prasilabs.dropme.backend.debug.ConsoleLog;

import java.io.IOException;
import java.util.List;

/**
 * Created by prasi on 5/6/16.
 */
public class GcmSender
{
    private static final String TAG = GcmSender.class.getSimpleName();

    private static final String API_KEY = "AIzaSyDLWzUM17yyaVWWAAw0t5m06wVj3WnYuNA";//System.getProperty("gcm.api.key");
    private static Sender sender;

    public static boolean sendGcmMessage(long id, String jobType, String msg, String gcmId)
    {
        ConsoleLog.i(TAG, "sending gcm");
        boolean success = false;

        if(sender == null)
        {
            sender = new Sender(API_KEY);
        }

        GcmRecord gcmRecord = OfyService.ofy().load().type(GcmRecord.class).filter(GcmRecord.GCM_ID_STR, gcmId).first().now();

        if(gcmRecord != null) {
            Message message = new Message.Builder().addData(GcmConstants.ID_KEY, String.valueOf(id)).addData(GcmConstants.JOB_TYPE_KEY, jobType).addData(GcmConstants.MESSAGE_KEY, msg).build();

            try {
                Result result = sender.send(message, gcmRecord.getGcmId(), 2);

                if (result.getMessageId() != null) {
                    ConsoleLog.i(TAG, "message sent to " + gcmRecord.getGcmId());
                    String canonicalId = result.getCanonicalRegistrationId();
                    if (canonicalId != null) {
                        ConsoleLog.i(TAG, "gcmID changed");
                        gcmRecord.setGcmId(canonicalId);
                        OfyService.ofy().save().entity(gcmRecord).now();
                    }

                    success = true;
                } else {
                    String error = result.getErrorCodeName();
                    if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                        ConsoleLog.w(TAG, " registration id " + gcmRecord.getGcmId() + " is invalid");
                        OfyService.ofy().delete().entity(gcmRecord).now();
                    } else {
                        ConsoleLog.w(TAG, "Error sending gcm message to " + gcmRecord.getGcmId());
                    }
                }
            } catch (Exception e) {
                ConsoleLog.e(e);
            }

            ConsoleLog.i(TAG, "gcmm sent with status " + success);
        }
        else
        {
            ConsoleLog.w(TAG, "gcm record is null");
        }

        return success;
    }

    public static boolean sendGcmMessage(long id, String msg, String jobType, List<String> gcmIDs)
    {
        boolean isSuccess = false;
        if(sender == null)
        {
            sender = new Sender(API_KEY);
        }

        Message message = new Message.Builder().addData(GcmConstants.ID_KEY, String.valueOf(id)).addData(GcmConstants.JOB_TYPE_KEY, jobType).addData(GcmConstants.MESSAGE_KEY, msg).build();

        try
        {
            MulticastResult multicastResult = sender.send(message, gcmIDs, 2);


            ConsoleLog.i(TAG, "total gcm sent is : " + multicastResult.getTotal());
            ConsoleLog.i(TAG, "total gcm success is : " + multicastResult.getSuccess());
            if(multicastResult.getFailure() > 0)
            {
                ConsoleLog.w(TAG, "total gcm failure is : " + multicastResult.getFailure());
            }
            if(multicastResult.getCanonicalIds() > 0)
            {
                ConsoleLog.w(TAG, "total gcm canonicals is : " + multicastResult.getCanonicalIds());
            }

            if(multicastResult.getSuccess() > 0)
            {
                isSuccess = true;
            }
        }
        catch (IOException e)
        {
            ConsoleLog.e(e);
        }

        return isSuccess;
    }
}
