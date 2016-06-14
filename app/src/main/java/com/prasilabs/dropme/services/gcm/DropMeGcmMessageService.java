package com.prasilabs.dropme.services.gcm;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;
import com.prasilabs.constants.GcmConstants;
import com.prasilabs.constants.PushMessageJobType;
import com.prasilabs.dropme.db.dbPojos.DropMeNotifs;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.modelengines.DropMeNotifModelEngine;
import com.prasilabs.dropme.services.notification.DropMeNotifCreator;
import com.prasilabs.gcmData.LocationSharePush;
import com.prasilabs.gcmData.RideInfoPush;

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

        long id = data.getLong(GcmConstants.ID_KEY);
        String type = data.getString(GcmConstants.JOB_TYPE_KEY);
        String content = data.getString(GcmConstants.MESSAGE_KEY);

        DropMeNotifs dropMeNotifs = new DropMeNotifs(id, type, content);
        DropMeNotifModelEngine.getInstance().addDropMeNotif(dropMeNotifs, null);

        if(type != null)
        {
            if (type.equals(PushMessageJobType.RIDE_FOUND_ALERT_STR))
            {
                RideInfoPush rideInfoPush = new Gson().fromJson(content, RideInfoPush.class);

                DropMeNotifCreator dropMeNotifCreator = new DropMeNotifCreator(id, "New Ride", "New ride found for the location " + rideInfoPush.getDestLocation());
                dropMeNotifCreator.buildNotification(this);
            }
            else if(type.equals(PushMessageJobType.LOCATION_SHARE_STR))
            {
                LocationSharePush locationSharePush = new Gson().fromJson(content, LocationSharePush.class);

                DropMeNotifCreator dropMeNotifCreator = new DropMeNotifCreator(id, "Location Share", locationSharePush.getLoceeName() + " shared location with you");
                dropMeNotifCreator.buildNotification(this);
            }
        }


    }

}
