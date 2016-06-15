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
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.prasilabs.constants.AuthConstants;
import com.prasilabs.dropme.backend.debug.ConsoleLog;
import com.prasilabs.dropme.backend.debug.Experiments;
import com.prasilabs.dropme.backend.io.ApiResponse;
import com.prasilabs.dropme.backend.io.GcmRecordIO;
import com.prasilabs.dropme.backend.io.MyRideInfo;
import com.prasilabs.dropme.backend.io.RideAlertIo;
import com.prasilabs.dropme.backend.io.RideDetail;
import com.prasilabs.dropme.backend.io.RideInput;
import com.prasilabs.dropme.backend.io.VDropMeUser;
import com.prasilabs.dropme.backend.io.VVehicle;
import com.prasilabs.dropme.backend.logicEngines.DropMeUserLogicEngine;
import com.prasilabs.dropme.backend.logicEngines.GcmLogicEngine;
import com.prasilabs.dropme.backend.logicEngines.RideAlertLogicEngine;
import com.prasilabs.dropme.backend.logicEngines.RideLogicEngine;
import com.prasilabs.dropme.backend.logicEngines.UserLocationLogicEngine;
import com.prasilabs.dropme.backend.logicEngines.VehicleLogicEngine;
import com.prasilabs.dropme.backend.security.DropMeAuthenticator;
import com.prasilabs.dropme.backend.security.FBAuthenticator;
import com.prasilabs.dropme.backend.utils.AdminUtil;

import java.util.List;

import static com.google.api.server.spi.config.ApiMethod.HttpMethod.POST;

/** An endpoint class we are exposing */
@Api(
        name = "dropMeApi",
        version = "v1",
        clientIds = {AuthConstants.WEB_CLIENT_ID,  AuthConstants.ANDROID_PROD_CLIENT_ID, AuthConstants.ANDROID_DEV_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID},
        audiences = {AuthConstants.ANDROID_AUDIENCE},
        scopes = {AuthConstants.EMAIL_SCOPE},
        authenticators = {DropMeAuthenticator.class, FBAuthenticator.class, EndpointsAuthenticator.class}, //add EndpointsAuthenticator to the end of the @authenticators list to make it as a fallback when user provided authenticator fails. Endpoints will try all authenticators and return the first successful one.

        namespace = @ApiNamespace(
                ownerDomain = "backend.dropme.prasilabs.com",
                ownerName = "backend.dropme.prasilabs.com",
                packagePath=""
        )
)
public class DropMeEndPoint
{
    private static final String TAG = DropMeEndPoint.class.getSimpleName();

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
            return DropMeUserLogicEngine.getInstance().getVDropMeUserById(id);
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
            return VehicleLogicEngine.getInstance().getVVehicleById(id);
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }

        return null;
    }

    @ApiMethod(name = "addVehicle")
    public ApiResponse addVehicle(User user, VVehicle vVehicle) throws OAuthRequestException
    {
        AdminUtil.checkAndThrow(user);

        ApiResponse apiResponse = new ApiResponse();

        try
        {
            apiResponse = VehicleLogicEngine.getInstance().addVehicle(user, vVehicle);
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }

        return apiResponse;
    }

    @ApiMethod(name = "createRide", path = "createRide", httpMethod = POST)
    public RideInput createRide(User user, RideInput rideInput) throws OAuthRequestException
    {
        AdminUtil.checkAndThrow(user);

        try
        {
            return RideLogicEngine.getInstance().createRide(user, rideInput);
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }

        return null;
    }

    @ApiMethod(name = "updateRide")
    public ApiResponse updateRide(User user,RideInput rideInput) throws OAuthRequestException
    {
        AdminUtil.checkAndThrow(user);

        ApiResponse apiResponse = new ApiResponse();

        try
        {
            apiResponse = RideLogicEngine.getInstance().updateRide(user,rideInput);
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }

        return apiResponse;
    }

    @ApiMethod(name = "getRideDetail")
    public RideDetail getRideDetail(@Named("rideId") long rideId)
    {
        try
        {
            ConsoleLog.l(TAG, "ride id is : "+ rideId);
            return RideLogicEngine.getInstance().getRideDetail(rideId);
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }

        return null;
    }

    @ApiMethod(name = "getCurrentRide")
    public RideInput getCurrentRide(User user, @Named("deviceId") String deviceId) throws OAuthRequestException
    {
        AdminUtil.checkAndThrow(user);

        try
        {
            return RideLogicEngine.getInstance().getCurrentRide(user, deviceId);
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }

        return null;
    }

    @ApiMethod(name = "cancelRide")
    public ApiResponse cancelRide(User user, @Named("deviceId") String deviceId) throws OAuthRequestException
    {
        AdminUtil.checkAndThrow(user);

        try
        {
            return RideLogicEngine.getInstance().cancelRide(user, deviceId);
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }

        return null;
    }

    @ApiMethod(name = "getRideDetailList",path = "getRideDetailList", httpMethod = POST)
    public List<RideDetail> getRideDetailList(User user, @Named("ids") List<Long> ids, GeoPt dest) throws OAuthRequestException
    {
        AdminUtil.checkAndThrow(user);

        try
        {
            List<RideDetail> rideDetailList = RideLogicEngine.getInstance().getRideDetailList(ids, dest);
            if(rideDetailList != null)
            {
                ConsoleLog.i(TAG, "list size is : " + rideDetailList.size());
            }
            else
            {
                ConsoleLog.i(TAG, "list is null");
            }
            return rideDetailList;
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }

        return null;
    }

    @ApiMethod(name = "addGcmId")
    public ApiResponse addGcmId(User user, GcmRecordIO gcmRecordIO) throws OAuthRequestException
    {
        AdminUtil.checkAndThrow(user);

        ApiResponse apiResponse = new ApiResponse();

        try
        {
            apiResponse = GcmLogicEngine.getInstance().addGcmRecord(gcmRecordIO);
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }

        return apiResponse;
    }

    @ApiMethod(name = "createAlert")
    public ApiResponse createAlert(User user, RideAlertIo rideAlertIo) throws OAuthRequestException
    {
        AdminUtil.checkAndThrow(user);

        ApiResponse apiResponse = new ApiResponse();

        try
        {
            apiResponse = RideAlertLogicEngine.getInstance().createRideAlert(user, rideAlertIo);
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }

        return apiResponse;
    }

    @ApiMethod(name = "updateUserLocation")
    public ApiResponse updateUserLocation(User user, @Named("deviceId") String deviceId, GeoPt currentLoc)
    {
        ApiResponse apiResponse = new ApiResponse();

        try
        {
            apiResponse = UserLocationLogicEngine.getInstance().updateUserLocation(user, deviceId, currentLoc);
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }

        return apiResponse;
    }

    @ApiMethod(name = "getRideListOfUser")
    public List<MyRideInfo> getRideListOfUser(User user, @Named("skip") int skip, @Named("pageSize") int pageSize) throws OAuthRequestException
    {
        AdminUtil.checkAndThrow(user);

        try
        {
            return RideLogicEngine.getInstance().getRideDetailsOfUser(user,skip,pageSize);
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }

        return null;

    }

    @ApiMethod(name = "getAlertListOfUser")
    public List<RideAlertIo> getRideAlertsOfUser(User user) throws OAuthRequestException
    {
        AdminUtil.checkAndThrow(user);

        try
        {
            return RideAlertLogicEngine.getInstance().getRideAlerts(user);
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }

        return null;
    }

    @ApiMethod(name = "shareLocation")
    public ApiResponse shareLocation(User user, @Named("userId") long recieverId, @Named("deviceID") String deviceID) throws OAuthRequestException {
        AdminUtil.checkAndThrow(user);

        try {
            UserLocationLogicEngine.getInstance().shareLocation(user, recieverId, deviceID);
        } catch (Exception e) {
            ConsoleLog.e(e);
        }

        return null;
    }

    @ApiMethod(name = "test")
    public void test(@Named("password") String password) throws OAuthRequestException
    {
        if(password.equals("prasi12345"))
        {
            Experiments.test();
        }
        else
        {
            ConsoleLog.w(TAG, "password is wrong");
            throw new OAuthRequestException("password is wrong");
        }
    }
}
