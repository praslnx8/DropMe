package com.prasilabs.dropme.services.oauth;

/**
 * Created by prasi on 26/5/16.
 */
public class FCredential
{
    private static String prevAccesToken;
    private static FbCredentialInitializer fbCredentialInitializer;

    public static FbCredentialInitializer getFbAccountCredential(String accesToken)
    {
        if(prevAccesToken != null && prevAccesToken.equals(accesToken) && fbCredentialInitializer != null)
        {
            return fbCredentialInitializer;
        }
        else
        {
            fbCredentialInitializer = new FbCredentialInitializer(accesToken);
            prevAccesToken = accesToken;
        }

        return fbCredentialInitializer;
    }
}
