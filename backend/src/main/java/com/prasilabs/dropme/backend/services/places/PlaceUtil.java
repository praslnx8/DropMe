package com.prasilabs.dropme.backend.services.places;

import com.google.appengine.api.datastore.GeoPt;
import com.prasilabs.apiRequest.ApiFetcher;
import com.prasilabs.apiRequest.ApiRequestType;
import com.prasilabs.apiRequest.ApiResult;
import com.prasilabs.constants.ApiConstants;
import com.prasilabs.dropme.backend.debug.ConsoleLog;
import com.prasilabs.dropme.backend.utils.JsonUtil;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by prasi on 13/6/16.
 */
public class PlaceUtil
{
    private static final String API_URL = "https://maps.googleapis.com/maps/api/geocode/json?key=" + ApiConstants.SERVER_GOOGLE_API;
    private static final String TAG = PlaceUtil.class.getSimpleName();

    public static String getLocalityName(GeoPt geoPt)
    {
        String locality = null;

        Float lat = geoPt.getLatitude();
        Float lon = geoPt.getLongitude();

        String placeUrl = API_URL + "&latlng="+lat.doubleValue() + "," + lon.doubleValue();
        ApiResult apiResult = ApiFetcher.makeStringRequest(placeUrl, ApiRequestType.GET);

        if(apiResult.isSuccess())
        {
            String result = apiResult.getResult();

            JSONObject jsonObject = JsonUtil.createjsonobject(result);
            JSONArray jsonArray = JsonUtil.checkHasArray(jsonObject, "results");

            boolean isLocalityFound = false;
            for(int i = 0; i < jsonArray.length() ; i++)
            {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                JSONArray jsonArray1 = JsonUtil.checkHasArray(jsonObject1, "address_components");

                for(int i1 = 0; i1 < jsonArray1.length(); i1++)
                {
                    JSONArray types = JsonUtil.checkHasArray(jsonArray1.getJSONObject(i1), "types");

                    for(int i2 = 0; i2<types.length(); i2++)
                    {
                        if(types.get(i2).equals("sublocality"))
                        {
                            isLocalityFound = true;
                            break;
                        }
                    }

                    if(isLocalityFound)
                    {
                        locality = JsonUtil.checkHasString(jsonArray1.getJSONObject(i1), "short_name");
                        break;
                    }
                }

                if(isLocalityFound)
                {
                    break;
                }
            }
        }
        else
        {
            ConsoleLog.i(TAG, "api request error");
            ConsoleLog.s(TAG, "error is :" + apiResult.getError());
        }



        return locality;
    }
}
