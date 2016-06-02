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
import com.prasilabs.dropme.backend.debug.Experiments;
import com.prasilabs.dropme.backend.io.ApiResponse;
import com.prasilabs.dropme.backend.io.RideDetail;
import com.prasilabs.dropme.backend.io.RideInput;
import com.prasilabs.dropme.backend.io.VDropMeUser;
import com.prasilabs.dropme.backend.io.VVehicle;
import com.prasilabs.dropme.backend.logicEngines.DropMeUserLogicEngine;
import com.prasilabs.dropme.backend.logicEngines.RideLogicEngine;
import com.prasilabs.dropme.backend.logicEngines.VehicleLogicEngine;
import com.prasilabs.dropme.backend.security.FBAuthenticator;
import com.prasilabs.dropme.backend.utils.AdminUtil;

import java.util.List;

import static com.google.api.server.spi.config.ApiMethod.HttpMethod.POST;

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
    public RideInput createRide(@Named("hash") String hash, RideInput rideInput) throws OAuthRequestException
    {
        DropMeUser dropMeUser = DropMeUserLogicEngine.getInstance().getDropMeUserByHash(hash);

        if(dropMeUser != null)
        {
            rideInput.setUserId(dropMeUser.getId());
            return RideLogicEngine.getInstance().createRide(rideInput);
        }
        else
        {
            throw new OAuthRequestException("User is not found. User needs to logged in");
        }
    }

    @ApiMethod(name = "getRideDetail")
    public RideDetail getRideDetail(@Named("rideId") long rideId)
    {
        return RideLogicEngine.getInstance().getRideDetail(rideId);
    }

    @ApiMethod(name = "getCurrentRide")
    public RideInput getCurrentRide(@Named("hash") String hash, @Named("deviceId") String deviceId) throws OAuthRequestException
    {
        DropMeUser dropMeUser = DropMeUserLogicEngine.getInstance().getDropMeUserByHash(hash);

        if(dropMeUser != null)
        {
            return RideLogicEngine.getInstance().getCurrentRide(dropMeUser, deviceId);
        }
        else
        {
            throw new OAuthRequestException("User is not found. User needs to logged in");
        }
    }

    @ApiMethod(name = "cancelRide")
    public ApiResponse cancelRide(@Named("hash") String hash, @Named("rideId") long rideId) throws OAuthRequestException
    {
        DropMeUser dropMeUser = DropMeUserLogicEngine.getInstance().getDropMeUserByHash(hash);

        if(dropMeUser != null)
        {
            return RideLogicEngine.getInstance().cancelRide(dropMeUser, rideId);
        }
        else
        {
            throw new OAuthRequestException("User is not found. User needs to logged in");
        }
    }

    @ApiMethod(name = "getRideDetailList",path = "getRideDetailList", httpMethod = POST)
    public List<RideDetail> getRideDetailList(@Named("hash") String hash, @Named("ids") List<Long> ids) throws OAuthRequestException
    {
        DropMeUser dropMeUser = DropMeUserLogicEngine.getInstance().getDropMeUserByHash(hash);

        if(dropMeUser != null)
        {
            return RideLogicEngine.getInstance().getRideDetailList(ids);
        }
        else
        {
            throw new OAuthRequestException("User is not found. User needs to logged in");
        }
    }



    @ApiMethod(name = "test")
    public void test(@Named("password") String password)
    {
        if(password.equals("prasi123"))
        {
            Experiments.test();
        }
    }
}
