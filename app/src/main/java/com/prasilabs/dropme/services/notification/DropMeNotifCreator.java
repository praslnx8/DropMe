package com.prasilabs.dropme.services.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.prasilabs.dropme.R;
import com.prasilabs.dropme.activities.GenericActivity;
import com.prasilabs.dropme.activities.HomeActivity;

/**
 * Created by prasi on 13/6/16.
 */
public class DropMeNotifCreator
{
    private long id;
    private String title;
    private String message;

    public DropMeNotifCreator(long id, String title, String message)
    {
        this.id = id;
        this.title = title;
        this.message = message;
    }

    public void buildNotification(Context context)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.drawable.ic_directions_walk_black_24dp);
        builder.setContentTitle(title);
        builder.setContentText(message);

        Intent resultIntent = new Intent(context, GenericActivity.class);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(HomeActivity.class);
        taskStackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) id, builder.build());
    }
}
