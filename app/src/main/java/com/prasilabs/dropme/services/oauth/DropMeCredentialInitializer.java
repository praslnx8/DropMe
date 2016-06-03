package com.prasilabs.dropme.services.oauth;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.prasilabs.constants.CommonConstant;

import java.io.IOException;

/**
 * Created by prasi on 3/6/16.
 */
public class DropMeCredentialInitializer implements HttpRequestInitializer
{
    private String hash;
    public DropMeCredentialInitializer(String hash)
    {
        this.hash = hash;
    }

    @Override
    public void initialize(HttpRequest request) throws IOException
    {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put(CommonConstant.HASHAUTHHEADER, hash);
        request.setHeaders(httpHeaders);
    }
}
