package com.prasilabs.dropme.backend.logicEngines;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.prasilabs.util.ValidateUtil;
import com.prasilabs.dropme.backend.core.CoreLogicEngine;
import com.prasilabs.dropme.backend.datastore.DropMeUser;
import com.prasilabs.dropme.backend.db.OfyService;
import com.prasilabs.dropme.backend.io.VDropMeUser;
import com.prasilabs.dropme.backend.security.HashGenerator;

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

    public VDropMeUser loginSignup(User user, VDropMeUser input)
    {
        DropMeUser existingDropMeUser = OfyService.ofy().load().type(DropMeUser.class).filter(DropMeUser.EMAIL_STR, user.getEmail()).first().now();

        if(existingDropMeUser == null)
        {
            input.setEmail(user.getEmail());
            DropMeUser dropMeUser = convertToDropMeUSer(input);

            if (validateDropMeUser(dropMeUser))
            {

                dropMeUser.setHash(HashGenerator.md5(String.valueOf(System.currentTimeMillis())));
                dropMeUser.setCreated(new Date(System.currentTimeMillis()));
                Key<DropMeUser> dropMeUserKey = OfyService.ofy().save().entity(dropMeUser).now();
                dropMeUser.setId(dropMeUserKey.getId());

                return convertToVDropMeUser(dropMeUser, true);
            }

            return null;
        }
        else
        {
            return convertToVDropMeUser(existingDropMeUser, true);
        }
    }

    public VDropMeUser getDropMeUserById(long id)
    {
        DropMeUser dropMeUser = getDropMeUser(id);

        VDropMeUser vDropMeUser = convertToVDropMeUser(dropMeUser, false);

        return vDropMeUser;
    }

    public DropMeUser getDropMeUser(long id)
    {
        DropMeUser dropMeUser = OfyService.ofy().load().type(DropMeUser.class).id(id).now();

        return dropMeUser;
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
            isValid= true;

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
            dropMeUser.setLoginType(vDropMeUser.getLoginType());
            dropMeUser.setGender(vDropMeUser.getGender());
            dropMeUser.setMobile(vDropMeUser.getMobile());
            dropMeUser.setMobileVerified(vDropMeUser.isMobileVerified());

            return dropMeUser;
        }

        return null;
    }

    private static VDropMeUser convertToVDropMeUser(DropMeUser dropMeUser, boolean isSendHash)
    {
        VDropMeUser vDropMeUser = new VDropMeUser();

        vDropMeUser.setId(dropMeUser.getId());
        if(isSendHash)
        {
            vDropMeUser.setHash(dropMeUser.getHash());
        }
        vDropMeUser.setName(dropMeUser.getName());
        vDropMeUser.setEmail(dropMeUser.getEmail());
        vDropMeUser.setGender(dropMeUser.getGender());
        vDropMeUser.setLoginType(dropMeUser.getLoginType());
        vDropMeUser.setMobile(dropMeUser.getMobile());
        vDropMeUser.setRoles(dropMeUser.getRoles());
        vDropMeUser.setCreated(dropMeUser.getCreated());
        vDropMeUser.setMobileVerified(dropMeUser.isMobileVerified());

        return vDropMeUser;
    }
}
