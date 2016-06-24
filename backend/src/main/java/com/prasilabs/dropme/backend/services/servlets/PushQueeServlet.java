package com.prasilabs.dropme.backend.services.servlets;

import com.prasilabs.dropme.backend.debug.ConsoleLog;
import com.prasilabs.dropme.backend.logicEngines.DropMeUserLogicEngine;
import com.prasilabs.dropme.backend.logicEngines.RideAlertLogicEngine;
import com.prasilabs.dropme.backend.services.pushquees.PushQueueController;
import com.prasilabs.util.DataUtil;

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
        else if(operationType.equals(PushQueueController.SEND_RIDE_ALERT_OPER))
        {
            String srideId = req.getParameter(PushQueueController.ID_KEY);

            long rideID = DataUtil.stringToLong(srideId);

            RideAlertLogicEngine.getInstance().sendRideAlertsForRide(rideID);
        } else if (operationType.equals(PushQueueController.NEW_REG_EMAIL_OPER)) {
            String userId = req.getParameter(PushQueueController.ID_KEY);

            long userID = DataUtil.stringToLong(userId);

            DropMeUserLogicEngine.getInstance().sendNewRegistrationEmail(userID);
        }
        else
        {
            ConsoleLog.w(TAG, "unsupported operation. Exiting");
        }
    }
}
