package com.prasilabs.dropme.backend.utils;

import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.prasilabs.dropme.backend.datastore.DropMeUser;
import com.prasilabs.dropme.backend.db.OfyService;
import com.prasilabs.dropme.backend.debug.ConsoleLog;
import com.prasilabs.enums.UserRole;

/**
 * Created by prasi on 19/4/16.
 * admin util to check wether user object exist. admin or not
 */
public class AdminUtil
{
    public static boolean checkAdmin(User user)
    {
        try
        {
            return checkAndThrow(user);
        }
        catch (OAuthRequestException e)
        {
            return false;
        }
    }

    public static boolean checkAndThrow(User user) throws OAuthRequestException
    {
        return checkAndThrow(user, false);
    }

    public static boolean checkAndThrow(User user, boolean isAdminCheck) throws OAuthRequestException
    {
        boolean isAdmin = false;
        if(user != null && user.getEmail() != null)
        {
            if(isAdminCheck)
            {
                DropMeUser dropMeUser = OfyService.ofy().load().type(DropMeUser.class).filter(DropMeUser.EMAIL_STR, user.getEmail()).first().now();
                if (dropMeUser != null && dropMeUser.getRoles() != null && dropMeUser.getRoles().contains(UserRole.Admin.name()))
                {
                    isAdmin = true;
                }
            }
        }
        else
        {
            OAuthRequestException oauthException = new OAuthRequestException("oauth user is not found.");
            ConsoleLog.e(oauthException);
            throw oauthException;
        }

        if(isAdminCheck && !isAdmin)
        {
            OAuthRequestException oauthException = new OAuthRequestException("User is not admin.");
            ConsoleLog.e(oauthException);
            throw oauthException;
        }

        return isAdmin;
    }
}
