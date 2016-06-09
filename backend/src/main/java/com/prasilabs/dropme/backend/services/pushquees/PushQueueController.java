package com.prasilabs.dropme.backend.services.pushquees;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.prasilabs.dropme.backend.datastore.Ride;

/**
 * Created by prasi on 2/6/16.
 */
public class PushQueueController
{
    public static final String OPERATION_TYPE = "OPERATION_TYPE";
    public static final String REMOVE_GEO_FIRE_LOC_OPER = "REMOVE_GEO_FIRE_LOC";
    public static final String SEND_RIDE_ALERT_OPER = "SEND_RIDE_SLERT";
    public static final String GEO_KEY = "GEO_KEY";
    public static final String RIDE_ID_KEY = "RIDE_ID";


    public static void callRemoveGeoPtPush(String geoKey)
    {
        Queue queue = QueueFactory.getDefaultQueue();
        TaskOptions taskOptions = getTaskOption();
        taskOptions.param(OPERATION_TYPE, REMOVE_GEO_FIRE_LOC_OPER);
        taskOptions.param(GEO_KEY, geoKey);
        queue.add(taskOptions);
    }

    public static void sendRideAlert(Ride ride)
    {
        Queue queue = QueueFactory.getDefaultQueue();
        TaskOptions taskOptions = getTaskOption();
        taskOptions.param(OPERATION_TYPE, SEND_RIDE_ALERT_OPER);
        taskOptions.param(RIDE_ID_KEY, String.valueOf(ride.getId()));
        queue.add(taskOptions);
    }

    private static TaskOptions getTaskOption()
    {
        return TaskOptions.Builder.withUrl("/PushQ");
    }


}
