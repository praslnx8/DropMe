package com.prasilabs.dropme.backend.services.pushquees;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

/**
 * Created by prasi on 2/6/16.
 */
public class PushQueueController
{
    public static final String OPERATION_TYPE = "OPERATION_TYPE";
    public static final String REMOVE_GEO_FIRE_LOC_OPER = "REMOVE_GEO_FIRE_LOC";
    public static final String GEO_KEY = "GEO_KEY";


    public static void callRemoveGeoPtPush(String geoKey)
    {
        Queue queue = QueueFactory.getDefaultQueue();
        TaskOptions taskOptions = getTaskOption();
        taskOptions.param(OPERATION_TYPE, REMOVE_GEO_FIRE_LOC_OPER);
        taskOptions.param(GEO_KEY, geoKey);
        queue.add(taskOptions);
    }

    private static TaskOptions getTaskOption()
    {
        return TaskOptions.Builder.withUrl("/PushQ");
    }


}
