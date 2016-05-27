package com.prasilabs.dropme.services.oauth;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.prasilabs.CommonConstant;

import java.io.IOException;

/**
 * Created by prasi on 26/5/16.
 */
public class FbAccountCredential implements HttpRequestInitializer
{
    private String accesToken;

    public FbAccountCredential(String accesToken)
    {
        this.accesToken = accesToken;
    }

    @Override
    public void initialize(HttpRequest request) throws IOException
    {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put(CommonConstant.FBAUTHHEADER, accesToken);
        request.setHeaders(httpHeaders);
    }
}
