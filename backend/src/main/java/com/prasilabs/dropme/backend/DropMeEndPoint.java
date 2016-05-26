/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.prasilabs.dropme.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.prasilabs.dropme.backend.debug.ConsoleLog;
import com.prasilabs.dropme.backend.io.ApiResponse;
import com.prasilabs.dropme.backend.io.VDropMeUser;
import com.prasilabs.dropme.backend.logicEngines.DropMeUserLogicEngine;
import com.prasilabs.dropme.backend.utils.AdminUtil;

/** An endpoint class we are exposing */
@Api(
  name = "dropMeApi",
  version = "v1",
  namespace = @ApiNamespace(
    ownerDomain = "backend.dropme.prasilabs.com",
    ownerName = "backend.dropme.prasilabs.com",
    packagePath=""
  )
)
public class DropMeEndPoint
{
    @ApiMethod(name = "signup")
    public ApiResponse signup(User user, VDropMeUser vDropMeUser) throws OAuthRequestException
    {
        AdminUtil.checkAndThrow(user);

        try
        {
            ApiResponse apiResponse = DropMeUserLogicEngine.getInstance().signup(user, vDropMeUser);
            return apiResponse;
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }

        return new ApiResponse();
    }

    @ApiMethod
    public VDropMeUser login(User user) throws OAuthRequestException
    {
        AdminUtil.checkAndThrow(user);

        try
        {
            return DropMeUserLogicEngine.getInstance().login(user);
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }

        return null;
    }
}
