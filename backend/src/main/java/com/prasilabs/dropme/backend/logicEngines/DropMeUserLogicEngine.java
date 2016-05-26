package com.prasilabs.dropme.backend.logicEngines;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.prasilabs.ValidateUtil;
import com.prasilabs.dropme.backend.core.CoreLogicEngine;
import com.prasilabs.dropme.backend.datastore.DropMeUser;
import com.prasilabs.dropme.backend.db.OfyService;
import com.prasilabs.dropme.backend.io.ApiResponse;
import com.prasilabs.dropme.backend.io.VDropMeUser;

import java.util.Date;

/**
 * Created by prasi on 26/5/16.
 */
public class DropMeUserLogicEngine extends CoreLogicEngine
{
   private static DropMeUserLogicEngine instance;

    public static DropMeUserLogicEngine getInstance()
    {
        if(instance == null)
        {
            instance = new DropMeUserLogicEngine();
        }
        return instance;
    }

    private DropMeUserLogicEngine(){}

    public ApiResponse signup(User user, VDropMeUser vDropMeUser)
    {
        ApiResponse apiResponse = new ApiResponse();

        DropMeUser existingDropMeUser = OfyService.ofy().load().type(DropMeUser.class).filter(DropMeUser.EMAIL_STR, user.getEmail()).first().now();

        if(existingDropMeUser == null)
        {
            vDropMeUser.setEmail(user.getEmail());
            DropMeUser dropMeUser = convertToDropMeUSer(vDropMeUser);

            if (validateDropMeUser(dropMeUser))
            {
                dropMeUser.setCreated(new Date(System.currentTimeMillis()));
                Key<DropMeUser> dropMeUserKey = OfyService.ofy().save().entity(dropMeUser).now();
                apiResponse.setStatus(true);
                apiResponse.setId(dropMeUserKey.getId());
                apiResponse.setMessage("Welcome " + dropMeUser.getName());
            }
        }
        else
        {
            apiResponse.setStatus(true);
            apiResponse.setId(existingDropMeUser.getId());
            apiResponse.setMessage("Welcome back " + existingDropMeUser.getName());
        }
        return apiResponse;
    }

    public VDropMeUser login(User user)
    {
        DropMeUser dropMeUser = getDropMeUser(user.getEmail());

        VDropMeUser vDropMeUser = convertToVDropMeUser(dropMeUser);

        return vDropMeUser;
    }

    public DropMeUser getDropMeUser(String email)
    {
        DropMeUser dropMeUser = OfyService.ofy().load().type(DropMeUser.class).filter(DropMeUser.EMAIL_STR, email).first().now();

        return dropMeUser;
    }

    private static boolean validateDropMeUser(DropMeUser dropMeUser)
    {
        boolean isValid = false;

        if(dropMeUser != null)
        {
            if (ValidateUtil.validateEmail(dropMeUser.getEmail()) && !ValidateUtil.isStringEmpty(dropMeUser.getName()) && dropMeUser.getGender() != 0)
            {
                isValid = true;
            }
        }

        return isValid;
    }

    private static DropMeUser convertToDropMeUSer(VDropMeUser vDropMeUser)
    {
        if(vDropMeUser != null)
        {
            DropMeUser dropMeUser = new DropMeUser();

            dropMeUser.setId(vDropMeUser.getId());
            dropMeUser.setName(vDropMeUser.getName());
            dropMeUser.setEmail(vDropMeUser.getEmail());
            dropMeUser.setGender(vDropMeUser.getGender());
            dropMeUser.setMobile(vDropMeUser.getMobile());
            dropMeUser.setMobileVerified(vDropMeUser.isMobileVerified());

            return dropMeUser;
        }

        return null;
    }

    private static VDropMeUser convertToVDropMeUser(DropMeUser dropMeUser)
    {
        VDropMeUser vDropMeUser = new VDropMeUser();

        vDropMeUser.setId(dropMeUser.getId());
        vDropMeUser.setName(dropMeUser.getName());
        vDropMeUser.setEmail(dropMeUser.getEmail());
        vDropMeUser.setGender(vDropMeUser.getGender());
        vDropMeUser.setMobile(dropMeUser.getMobile());
        vDropMeUser.setRoles(dropMeUser.getRoles());
        vDropMeUser.setCreated(dropMeUser.getCreated());
        vDropMeUser.setMobileVerified(dropMeUser.isMobileVerified());

        return vDropMeUser;
    }
}
