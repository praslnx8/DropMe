package com.prasilabs.dropme.backend.services.servlets;

import com.prasilabs.dropme.backend.debug.ConsoleLog;
import com.prasilabs.dropme.backend.services.pushquees.PushQueueController;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by prasi on 2/6/16.
 */
public class PushQueeServlet extends HttpServlet
{
    private static final String TAG = PushQueeServlet.class.getSimpleName();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        ConsoleLog.l(TAG, "post called");

        String operationType = req.getParameter(PushQueueController.OPERATION_TYPE);

        if(operationType.equals(PushQueueController.REMOVE_GEO_FIRE_LOC_OPER))
        {
            String key = req.getParameter(PushQueueController.GEO_KEY);
            //TODO not working. Error bacjenf instance...
            // Wait for Geofire to work on auto backend instance. calling backend instance has lots of procedures
            // GeoFireManager.removeGeoPoint(key);
        }
        else
        {
            ConsoleLog.w(TAG, "unsupported operation. Exiting");
        }
    }
}
