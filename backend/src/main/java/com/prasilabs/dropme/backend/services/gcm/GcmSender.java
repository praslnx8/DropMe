package com.prasilabs.dropme.backend.services.gcm;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.prasilabs.dropme.backend.datastore.GcmRecord;
import com.prasilabs.dropme.backend.db.OfyService;
import com.prasilabs.dropme.backend.debug.ConsoleLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by prasi on 5/6/16.
 */
public class GcmSender
{
    private static final String API_KEY = System.getProperty("gcm.api.key");
    private static final String TAG = GcmSender.class.getSimpleName();
    private static Sender sender;


    public static boolean sendGcmMessage(String msg, GcmRecord gcmRecord)
    {
        boolean success = false;

        if(sender == null)
        {
            sender = new Sender(API_KEY);
        }

        Message message = new Message.Builder().addData("message", msg).build();

        try
        {
            Result result = sender.send(message, gcmRecord.getGcmId(), 2);

            if(result.getMessageId() != null)
            {
                ConsoleLog.is(TAG, "message sent to " + gcmRecord.getGcmId());
                String canonicalId = result.getCanonicalRegistrationId();
                if(canonicalId != null)
                {
                    ConsoleLog.is(TAG, "gcmID changed");
                    gcmRecord.setGcmId(canonicalId);
                    OfyService.ofy().save().entity(gcmRecord).now();
                }

                success = true;
            }
            else
            {
                String error = result.getErrorCodeName();
                if(error.equals(Constants.ERROR_NOT_REGISTERED))
                {
                    ConsoleLog.w(TAG, " registration id "+ gcmRecord.getGcmId() +" is invalid");
                    OfyService.ofy().delete().entity(gcmRecord).now();
                }
                else
                {
                    ConsoleLog.w(TAG, "Error sending gcm message to " + gcmRecord.getGcmId());
                }
            }
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }

        return success;
    }

    public static boolean sendGcmMessage(String msg, List<GcmRecord> gcmRecords)
    {
        if(sender == null)
        {
            sender = new Sender(API_KEY);
        }

        Message message = new Message.Builder().addData("message", msg).build();

        List<String> gcmIDs = new ArrayList<>();

        for(GcmRecord gcmRecord : gcmRecords)
        {
            gcmIDs.add(gcmRecord.getGcmId());
        }

        try
        {
            MulticastResult multicastResult = sender.send(message, gcmIDs, 2);




        }
        catch (IOException e)
        {
            ConsoleLog.e(e);
        }

        return true;
    }
}
