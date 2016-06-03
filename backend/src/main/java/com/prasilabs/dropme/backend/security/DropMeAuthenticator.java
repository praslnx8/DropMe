package com.prasilabs.dropme.backend.security;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Authenticator;
import com.prasilabs.constants.CommonConstant;
import com.prasilabs.dropme.backend.datastore.DropMeUser;
import com.prasilabs.dropme.backend.logicEngines.DropMeUserLogicEngine;

import org.apache.http.util.TextUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by prasi on 3/6/16.
 */
public class DropMeAuthenticator implements Authenticator
{

    @Override
    public User authenticate(HttpServletRequest httpServletRequest)
    {
        String hash = httpServletRequest.getHeader(CommonConstant.HASHAUTHHEADER);

        if(!TextUtils.isEmpty(hash))
        {
            DropMeUser dropMeUser = DropMeUserLogicEngine.getInstance().getDropMeUserByHash(hash);

            if(dropMeUser != null && dropMeUser.getEmail() != null)
            {
                User user = new User(dropMeUser.getName(), dropMeUser.getEmail());
                return user;
            }
        }

        return null;
    }


}
