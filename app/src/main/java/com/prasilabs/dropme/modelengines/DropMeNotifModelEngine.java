package com.prasilabs.dropme.modelengines;

import com.prasilabs.constants.PushMessageJobType;
import com.prasilabs.dropme.core.CoreApp;
import com.prasilabs.dropme.core.CoreModelEngine;
import com.prasilabs.dropme.db.dbPojos.DropMeNotifs;
import com.prasilabs.dropme.db.dbmanage.DatabaseHelper;
import com.prasilabs.dropme.debug.ConsoleLog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Created by prasi on 14/6/16.
 */
public class DropMeNotifModelEngine extends CoreModelEngine
{
    private static final String TAG = DropMeNotifModelEngine.class.getSimpleName();

    private static DropMeNotifModelEngine instance;

    private DropMeNotifModelEngine(){}

    public static DropMeNotifModelEngine getInstance()
    {
        if(instance == null)
        {
            instance = new DropMeNotifModelEngine();
        }

        return instance;
    }

    public void addDropMeNotif(final DropMeNotifs dropMeNotifs, final AddNotifCallBack addNotifCallBack)
    {
        new DatabaseHelper(CoreApp.getAppContext()).insertNotificationAsync(dropMeNotifs, new DatabaseHelper.DatabaseHandler<Long>() {
            @Override
            public void onComplete(boolean success, Long result)
            {
                if(addNotifCallBack != null)
                {
                    addNotifCallBack.status(success);
                }
            }
        });
    }

    public void getNotification(final GetNotifCallBack getNotifCallBack)
    {

        new DatabaseHelper(CoreApp.getAppContext()).getNotifListAsync(new DatabaseHelper.DatabaseHandler<List<DropMeNotifs>>() {
            @Override
            public void onComplete(boolean success, List<DropMeNotifs> dropMeNotifsList)
            {
                ConsoleLog.i(TAG, "notification list is  " + dropMeNotifsList);
                if (dropMeNotifsList != null)
                {
                    Iterator<DropMeNotifs> dropMeNotifsIterator = dropMeNotifsList.iterator();
                    while (dropMeNotifsIterator.hasNext()) {
                        DropMeNotifs dropMeNotifs = dropMeNotifsIterator.next();

                        Calendar yesCal = Calendar.getInstance();
                        yesCal.add(Calendar.DATE, -1);

                        Calendar minCal = Calendar.getInstance();
                        minCal.add(Calendar.MINUTE, -30);

                        if (dropMeNotifs.getCreatedTime() < yesCal.getTime().getTime()) {
                            new DatabaseHelper(CoreApp.getAppContext()).deleteNotifsAsync(dropMeNotifs.getId(), new DatabaseHelper.DatabaseHandler<Void>() {
                                @Override
                                public void onComplete(boolean success, Void result) {

                                }
                            });
                            dropMeNotifsIterator.remove();
                        } else if (dropMeNotifs.getJobType().equals(PushMessageJobType.LOCATION_SHARE_STR) && dropMeNotifs.getCreatedTime() < minCal.getTime().getTime()) {
                            new DatabaseHelper(CoreApp.getAppContext()).deleteNotifsAsync(dropMeNotifs.getId(), new DatabaseHelper.DatabaseHandler<Void>() {
                                @Override
                                public void onComplete(boolean success, Void result) {

                                }
                            });
                            dropMeNotifsIterator.remove();
                        }
                    }

                    if (getNotifCallBack != null) {
                        getNotifCallBack.getNotifs(dropMeNotifsList);
                    }
                } else
                {
                    if (getNotifCallBack != null) {
                        getNotifCallBack.getNotifs(new ArrayList<DropMeNotifs>());
                    }
                }
            }
        });

    }

    public interface GetNotifCallBack
    {
        void getNotifs(List<DropMeNotifs> dropMeNotifs);
    }

    public interface AddNotifCallBack
    {
        void status(boolean isSucess);
    }
}
