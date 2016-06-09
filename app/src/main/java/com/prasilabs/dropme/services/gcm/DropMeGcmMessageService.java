package com.prasilabs.dropme.services.gcm;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;
import com.prasilabs.dropme.debug.ConsoleLog;

/**
 * Created by prasi on 8/6/16.
 */
public class DropMeGcmMessageService extends GcmListenerService
{
    private static final String TAG = DropMeGcmMessageService.class.getSimpleName();

    public DropMeGcmMessageService() {
        super();
    }

    @Override
    public void onMessageReceived(String from, Bundle data)
    {
        super.onMessageReceived(from, data);

        ConsoleLog.i(TAG, "message recieved is : " + data.getString("message"));
    }

}
