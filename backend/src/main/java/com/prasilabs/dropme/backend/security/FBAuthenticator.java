package com.prasilabs.dropme.backend.security;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Authenticator;
import com.prasilabs.dropme.backend.debug.ConsoleLog;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by kiran on 10/14/2015.
 */
// Custom Authenticator class
public class FBAuthenticator implements Authenticator
{
    private static final String TAG = FBAuthenticator.class.getSimpleName();

    @Override
    public User authenticate(HttpServletRequest httpServletRequest)
    {
        ConsoleLog.i(TAG, "Received authentication request");
        String token = httpServletRequest.getHeader("Authorization");
        ConsoleLog.i(TAG, "Authorization token = " + token);

        if (token != null)
        {
            String email = authFacebookLogin(token);  // apply your Facebook auth.
            if (email != null)
            {
                return new User(email);
            }
        }
        return null;
    }

    public String authFacebookLogin(String accessToken)
    {
        String email = null;
        try
        {
            URL u = new URL("https://graph.facebook.com/me?" + accessToken);
            URLConnection c = u.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(c.getInputStream()));
            String inputLine;
            StringBuffer b = new StringBuffer();
            while ((inputLine = bufferedReader.readLine()) != null)
            {
                b.append(inputLine).append("\n");
            }
            bufferedReader.close();
            String graph = b.toString();

            JSONObject json = new JSONObject(graph);
            email = json.getString("email");
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }

        return email;
    }
}
