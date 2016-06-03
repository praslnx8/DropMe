package com.prasilabs.dropme.services.oauth;

import com.prasilabs.dropme.core.CoreApp;
import com.prasilabs.dropme.managers.UserManager;

/**
 * Created by prasi on 3/6/16.
 */
public class DropMeCredential
{
    private static String prevHash;
    private static DropMeCredentialInitializer dropMeCredentialInitializer;

    public static DropMeCredentialInitializer getDropMECredentialInitializer()
    {
        String hash = UserManager.getUserHash(CoreApp.getAppContext());
        if(hash != null)
        {
            if (dropMeCredentialInitializer == null || prevHash == null || !hash.equals(prevHash))
            {
                dropMeCredentialInitializer = new DropMeCredentialInitializer(hash);
                prevHash = hash;
            }
        }
        return dropMeCredentialInitializer;
    }
}
