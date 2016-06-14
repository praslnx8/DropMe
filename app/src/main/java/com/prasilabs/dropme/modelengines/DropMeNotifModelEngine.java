package com.prasilabs.dropme.modelengines;

import com.prasilabs.constants.PushMessageJobType;
import com.prasilabs.dropme.core.CoreModelEngine;
import com.prasilabs.dropme.db.dbPojos.DropMeNotifs;
import com.prasilabs.dropme.debug.ConsoleLog;

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
        callAsync(new AsyncCallBack()
        {
            @Override
            public Boolean async()
            {
                try
                {
                    long id = dropMeNotifs.save();
                    ConsoleLog.i(TAG, "db inserted id is : " + id);
                    return true;
                }
                catch (Exception e)
                {
                    ConsoleLog.e(e);
                }

                return false;
            }

            @Override
            public <T> void result(T t)
            {
                Boolean status = (Boolean) t;

                if(addNotifCallBack != null)
                {
                    if(status != null)
                    {
                        addNotifCallBack.status(true);
                    }
                    addNotifCallBack.status(false);
                }

            }
        });
    }

    public void getNotification(final GetNotifCallBack getNotifCallBack)
    {
        callAsync(new AsyncCallBack() {
            @Override
            public List<DropMeNotifs> async()
            {
                ConsoleLog.i(TAG, "start time is : " + System.currentTimeMillis());
                try
                {
                    List<DropMeNotifs> dropMeNotifsList = DropMeNotifs.listAll(DropMeNotifs.class);

                    Iterator<DropMeNotifs> dropMeNotifsIterator = dropMeNotifsList.iterator();
                    while (dropMeNotifsIterator.hasNext())
                    {
                        DropMeNotifs dropMeNotifs = dropMeNotifsIterator.next();

                        Calendar yesCal = Calendar.getInstance();
                        yesCal.add(Calendar.DATE, -1);

                        Calendar minCal = Calendar.getInstance();
                        minCal.add(Calendar.MINUTE, -30);

                        if(dropMeNotifs.getCreatedTime() < yesCal.getTime().getTime())
                        {
                            DropMeNotifs.delete(dropMeNotifs);
                            dropMeNotifsIterator.remove();
                        }
                        else if(dropMeNotifs.getJobType().equals(PushMessageJobType.LOCATION_SHARE_STR) && dropMeNotifs.getCreatedTime() < minCal.getTime().getTime())
                        {
                            DropMeNotifs.delete(dropMeNotifs);
                            dropMeNotifsIterator.remove();
                        }
                    }

                    ConsoleLog.i(TAG, "end time is " + System.currentTimeMillis());

                    return dropMeNotifsList;
                }
                catch (Exception e)
                {
                    ConsoleLog.e(e);
                }

                return null;
            }

            @Override
            public <T> void result(T t)
            {
                List<DropMeNotifs> dropMeNotifsList = (List<DropMeNotifs>) t;

                if(getNotifCallBack != null)
                {
                    getNotifCallBack.getNotifs(dropMeNotifsList);
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
