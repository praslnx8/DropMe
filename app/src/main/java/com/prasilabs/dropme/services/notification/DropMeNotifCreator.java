package com.prasilabs.dropme.services.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.prasilabs.dropme.R;
import com.prasilabs.dropme.activities.SplashActivity;
import com.prasilabs.dropme.debug.ConsoleLog;

/**
 * Created by prasi on 13/6/16.
 */
public class DropMeNotifCreator
{
    private static final String TAG = DropMeNotifCreator.class.getSimpleName();
    private int id;
    private String title;
    private String message;
    private Intent destIntent;

    public DropMeNotifCreator(int id, String title, String message, Intent intent)
    {
        this.id = id;
        this.title = title;
        this.message = message;
        this.destIntent = intent;
    }

    public static void cancelNotif(Context context, int id) {
        try {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(id);
        } catch (Exception e) {
            ConsoleLog.e(e);
        }
    }

    public void buildNotification(Context context)
    {
        try {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            builder.setSmallIcon(R.drawable.ic_directions_walk_black_24dp);
            builder.setContentTitle(title);
            builder.setContentText(message);

            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addParentStack(SplashActivity.class);
            taskStackBuilder.addNextIntent(destIntent);

            PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(resultPendingIntent);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(id, builder.build());
            ConsoleLog.i(TAG, "ntification notify done");
        } catch (Exception e) {
            ConsoleLog.e(e);
        }
    }
}
