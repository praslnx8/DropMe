package com.prasilabs.dropme.backend.debug;

import com.google.appengine.api.ThreadManager;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by prasi on 2/6/16.
 */
public class Experiments
{
    private static final String TAG = Experiments.class.getSimpleName();

    public static void test()
    {
        final AtomicLong counter = new AtomicLong();

        Thread thread = ThreadManager.createBackgroundThread(new Runnable() {
            public void run() {
                try {
                    for (int i=1;i<=10;i++)
                    {
                        ConsoleLog.i(TAG, "executing the " + i +"th time");
                        counter.incrementAndGet();
                        Thread.sleep(10);
                    }
                } catch (InterruptedException ex)
                {
                    ConsoleLog.e(ex);
                }
            }
        });
        thread.start();

        ConsoleLog.i(TAG, "test ended. Hope background will be still working....");
    }
}
