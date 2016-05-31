package com.prasilabs.dropme.backend.services.servlets;

import com.prasilabs.dropme.backend.debug.ConsoleLog;
import com.prasilabs.dropme.backend.services.imageUpload.BlobManager;

import org.json.JSONArray;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Kiran on 9/18/2015.
 */
public class UploadServlet extends HttpServlet
{
    private static final String TAG = UploadServlet.class.getSimpleName();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, OPTIONS, DELETE");
            response.addHeader("Access-Control-Allow-Headers", "Content-Type");
            response.addHeader("Access-Control-Max-Age", "86400");
            //response.setContentType("application/javascript; charset=UTF-8");
            response.setContentType("text/plain; charset=UTF-8");
            PrintWriter out = response.getWriter();
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");

            JSONArray jsonArray = BlobManager.getInstance().uploadBlob(request);

            if(jsonArray == null || !(jsonArray.length() > 0))
            {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            out.print(jsonArray);
            out.flush();
            out.close();
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "upload servlet to upload images to blob storage";
    }// </editor-fold>

}

