package com.prasilabs.apiRequest;

/**
 * Created by prasi on 10/3/16.
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ApiFetcher
{
    private ApiFetcher(){}


    public static ApiResult makeStringRequest(String urlString, ApiRequestType apiRequestType)
    {
        return makeStringRequest(urlString, apiRequestType, null, null);
    }

    public static ApiResult makeStringRequest(String urlString, ApiRequestType apiRequestType, String data, String encoding)
    {
        ApiResult apiResult = new ApiResult();
        try {
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if(encoding != null)
            {
                connection.setRequestProperty("Authorization", "Basic " + encoding);
            }
            connection.setRequestMethod(apiRequestType.name());
            connection.setDoOutput(true);

            // RZP: Simplified the data writing process
            if(data != null) {
                connection.getOutputStream().write(data.getBytes("UTF-8"));
            }

            int status = connection.getResponseCode();
            InputStream content;

            // RZP: You need to check for status code
            if(status >= 400)
            {
                content = connection.getErrorStream();
            }
            else
            {
                content = connection.getInputStream();
            }

            BufferedReader in = new BufferedReader (new InputStreamReader (content));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null)
            {
                stringBuilder.append(line);
            }
            in.close();

            String response = stringBuilder.toString();
            apiResult.setIsSuccess(true);
            apiResult.setResult(response);
        } catch (MalformedURLException e)
        {
            apiResult.setIsSuccess(false);
            apiResult.setError("MALFORMED URL EXCEPTION : " + e.getMessage());
        }
        catch (IOException e)
        {
            apiResult.setIsSuccess(false);
            apiResult.setError("IO EXCEPTION : " + e.getMessage());
        }

        return apiResult;
    }

}
