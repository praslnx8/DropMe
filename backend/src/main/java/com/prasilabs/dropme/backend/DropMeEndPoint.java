/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.prasilabs.dropme.backend;

import com.google.api.server.spi.auth.EndpointsAuthenticator;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.prasilabs.constants.AuthConstants;
import com.prasilabs.dropme.backend.datastore.DropMeUser;
import com.prasilabs.dropme.backend.debug.ConsoleLog;
import com.prasilabs.dropme.backend.io.ApiResponse;
import com.prasilabs.dropme.backend.io.VDropMeUser;
import com.prasilabs.dropme.backend.io.VRide;
import com.prasilabs.dropme.backend.io.VVehicle;
import com.prasilabs.dropme.backend.logicEngines.DropMeUserLogicEngine;
import com.prasilabs.dropme.backend.logicEngines.RideLogicEngine;
import com.prasilabs.dropme.backend.logicEngines.VehicleLogicEngine;
import com.prasilabs.dropme.backend.security.FBAuthenticator;
import com.prasilabs.dropme.backend.utils.AdminUtil;

/** An endpoint class we are exposing */
@Api(
        name = "dropMeApi",
        version = "v1",
        clientIds = {AuthConstants.WEB_CLIENT_ID,  AuthConstants.ANDROID_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID},
        audiences = {AuthConstants.ANDROID_AUDIENCE},
        scopes = {AuthConstants.EMAIL_SCOPE},
        authenticators = {FBAuthenticator.class, EndpointsAuthenticator.class}, //add EndpointsAuthenticator to the end of the @authenticators list to make it as a fallback when user provided authenticator fails. Endpoints will try all authenticators and return the first successful one.

        namespace = @ApiNamespace(
                ownerDomain = "backend.dropme.prasilabs.com",
                ownerName = "backend.dropme.prasilabs.com",
                packagePath=""
        )
)
public class DropMeEndPoint
{
    @ApiMethod(name = "loginsignup")
    public VDropMeUser loginsignup(User user, VDropMeUser vDropMeUser) throws OAuthRequestException
    {
        AdminUtil.checkAndThrow(user);

        try
        {
            return DropMeUserLogicEngine.getInstance().loginSignup(user, vDropMeUser);
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }

        return vDropMeUser;
    }

    @ApiMethod(name = "getUserDetail")
    public VDropMeUser getUserDetail(@Named("id") long id)
    {
        try
        {
            return DropMeUserLogicEngine.getInstance().getDropMeUserById(id);
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }

        return null;
    }

    @ApiMethod(name = "getVehicleDetail")
    public VVehicle getVehicleDetail(@Named("id") long id)
    {
        try
        {
            return VehicleLogicEngine.getInstance().getVehicleById(id);
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }

        return null;
    }

    @ApiMethod(name = "addVehicle")
    public ApiResponse addVehicle(@Named("hash") String hash, VVehicle vVehicle) throws OAuthRequestException
    {
        ApiResponse apiResponse = new ApiResponse();

        DropMeUser dropMeUser = DropMeUserLogicEngine.getInstance().getDropMeUserByHash(hash);

        if(dropMeUser != null)
        {
            vVehicle.setOwnerId(dropMeUser.getId());
            apiResponse = VehicleLogicEngine.getInstance().addVehicle(vVehicle);
        }
        else
        {
            throw new OAuthRequestException("User is not found. User needs to logged in");
        }

        return apiResponse;
    }

    @ApiMethod(name = "createRide")
    public ApiResponse createRide(@Named("hash") String hash, VRide vRide) throws OAuthRequestException
    {
        ApiResponse apiResponse = new ApiResponse();

        DropMeUser dropMeUser = DropMeUserLogicEngine.getInstance().getDropMeUserByHash(hash);

        if(dropMeUser != null)
        {
            vRide.setUserId(dropMeUser.getId());
            apiResponse = RideLogicEngine.getInstance().createRide(vRide);;
        }
        else
        {
            throw new OAuthRequestException("User is not found. User needs to logged in");
        }

        return apiResponse;
    }
}
