package com.prasilabs.dropme.backend.services.servlets;

import com.prasilabs.dropme.backend.debug.ConsoleLog;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by prasi on 31/5/16.
 */
public class CronJobServlet extends HttpServlet
{
    private static final String TAG = CronJobServlet.class.getSimpleName();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        ConsoleLog.l(TAG, "cron job servlet started");

        // TODO failed RideLogicEngine.getInstance().deleteInactiveGeoRideKeys();
    }
}
