package com.prasilabs.dropme.services.oauth;

/**
 * Created by prasi on 26/5/16.
 */
public class FCredential
{
    private static String prevAccesToken;
    private static FbAccountCredential fbAccountCredential;

    public static FbAccountCredential getFbAccountCredential(String accesToken)
    {
        if(prevAccesToken != null && prevAccesToken.equals(accesToken) && fbAccountCredential != null)
        {
            return fbAccountCredential;
        }
        else
        {
            fbAccountCredential = new FbAccountCredential(accesToken);
            prevAccesToken = accesToken;
        }

        return fbAccountCredential;
    }
}
