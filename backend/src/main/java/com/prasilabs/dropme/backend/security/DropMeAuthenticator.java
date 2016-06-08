package com.prasilabs.dropme.backend.security;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Authenticator;
import com.prasilabs.constants.CommonConstant;
import com.prasilabs.dropme.backend.datastore.DropMeUser;
import com.prasilabs.dropme.backend.debug.ConsoleLog;
import com.prasilabs.dropme.backend.logicEngines.DropMeUserLogicEngine;

import org.apache.http.util.TextUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by prasi on 3/6/16.
 */
public class DropMeAuthenticator implements Authenticator
{
    private static final String TAG = DropMeAuthenticator.class.getSimpleName();

    @Override
    public User authenticate(HttpServletRequest httpServletRequest)
    {
        String hash = httpServletRequest.getHeader(CommonConstant.HASHAUTHHEADER);

        if(!TextUtils.isEmpty(hash))
        {
            DropMeUser dropMeUser = DropMeUserLogicEngine.getInstance().getDropMeUserByHash(hash);

            if(dropMeUser != null && dropMeUser.getEmail() != null)
            {
                User user = new User(String.valueOf(dropMeUser.getId()), dropMeUser.getEmail());

                return user;
            }
            else
            {
                ConsoleLog.w(TAG, "hash passed but no valid user found");
            }
        }

        return null;
    }


}
