package com.prasilabs.dropme.modules.notifications.presenters;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.prasilabs.dropme.constants.BroadCastConstant;
import com.prasilabs.dropme.core.CorePresenter;
import com.prasilabs.dropme.db.dbPojos.DropMeNotifs;
import com.prasilabs.dropme.modelengines.DropMeNotifModelEngine;

import java.util.List;

/**
 * Created by prasi on 14/6/16.
 */
public class NotifPresenter extends CorePresenter
{
    private GetNotificationCallBack getNotificationCallBack;

    public NotifPresenter(GetNotificationCallBack getNotificationCallBack)
    {
        this.getNotificationCallBack = getNotificationCallBack;
    }

    @Override
    protected void onCreateCalled()
    {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadCastConstant.NOTIFICATION_REFRESH_INTENT);
        registerReciever(intentFilter);
    }

    @Override
    protected void broadCastRecieved(Context context, Intent intent)
    {

    }

    public void getNotifications()
    {
        DropMeNotifModelEngine.getInstance().getNotification(new DropMeNotifModelEngine.GetNotifCallBack() {
            @Override
            public void getNotifs(List<DropMeNotifs> dropMeNotifs)
            {
                if(getNotificationCallBack != null)
                {
                    if(dropMeNotifs != null && dropMeNotifs.size() > 0)
                    {
                        getNotificationCallBack.getNotifications(dropMeNotifs);
                    }
                    else
                    {
                        getNotificationCallBack.showEmpty();
                    }
                }
            }
        });
    }

    public interface GetNotificationCallBack
    {
        void getNotifications(List<DropMeNotifs> dropMeNotifsList);

        void showEmpty();
    }

}
