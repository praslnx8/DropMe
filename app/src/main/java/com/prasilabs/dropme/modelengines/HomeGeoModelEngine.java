package com.prasilabs.dropme.modelengines;

import com.firebase.client.FirebaseError;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.api.client.util.ArrayMap;
import com.prasilabs.dropme.backend.dropMeApi.model.VVehicle;
import com.prasilabs.dropme.core.CoreApp;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.enums.MarkerType;
import com.prasilabs.dropme.enums.UserOrVehicle;
import com.prasilabs.dropme.managers.UserManager;
import com.prasilabs.dropme.pojo.MarkerInfo;
import com.prasilabs.dropme.services.firebase.FireBaseConfig;
import com.prasilabs.enums.VehicleType;
import com.prasilabs.util.DataUtil;

import java.util.Map;

/**
 * Created by prasi on 27/5/16.
 */
public class HomeGeoModelEngine
{
    private static final double RADIUS_IN_KM = 0.5;
    private static final String GeoUserStr = "user";
    private static final String GeoVehStr = "veh";

    private static final String TAG = HomeGeoModelEngine.class.getSimpleName();



    private static HomeGeoModelEngine homeGeoModelEngine;
    private Map<String,MarkerInfo> geoMarkerMap = new ArrayMap<>();

    public static HomeGeoModelEngine getInstance()
    {
        if(homeGeoModelEngine == null)
        {
            homeGeoModelEngine = new HomeGeoModelEngine();
        }

        return homeGeoModelEngine;
    }

    private HomeGeoModelEngine(){}

    public void listenToGeoLoc(LatLng latLng, final GeoCallBack geoCallBack)
    {
        GeoQuery geoQuery = FireBaseConfig.getGeoFire().queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), RADIUS_IN_KM);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, final GeoLocation location)
            {
                MarkerInfo existingMarkerInfo = geoMarkerMap.get(key);
                if(existingMarkerInfo != null)
                {
                    existingMarkerInfo.setLoc(new LatLng(location.latitude, location.longitude));
                    if(geoCallBack != null)
                    {
                        geoCallBack.getMarker(existingMarkerInfo);
                    }
                }
                else {
                    final long id = getIdFromGeoKey(key);

                    if (key.contains(GeoUserStr)) {
                        if (id != UserManager.getDropMeUser(CoreApp.getAppContext()).getId()) {
                            MarkerInfo markerInfo = new MarkerInfo();
                            markerInfo.setId(id);
                            markerInfo.setLoc(new LatLng(location.latitude, location.longitude));
                            markerInfo.setMarkerType(MarkerType.User.name());
                            markerInfo.setUserOrVehicle(UserOrVehicle.User.name());

                            geoMarkerMap.put(key, markerInfo);

                            if (geoCallBack != null)
                            {
                                geoCallBack.getMarker(markerInfo);
                            }
                        }
                    } else if (key.contains(GeoVehStr)) {
                        VehicleModelEngine.getInstance().getVehicleDetail(id, new VehicleModelEngine.VehicleGetCallBack() {
                            @Override
                            public void getVehicle(VVehicle vVehicle) {
                                MarkerInfo markerInfo = new MarkerInfo();
                                markerInfo.setId(id);
                                markerInfo.setLoc(new LatLng(location.latitude, location.longitude));
                                if (vVehicle.getType().equals(VehicleType.Car.name())) {
                                    markerInfo.setMarkerType(MarkerType.Car.name());
                                } else if (vVehicle.getType().equals(VehicleType.Bike.name())) {
                                    markerInfo.setMarkerType(MarkerType.Bike.name());
                                }
                                markerInfo.setUserOrVehicle(UserOrVehicle.Vehicle.name());

                                geoMarkerMap.put(key, markerInfo);

                                if (geoCallBack != null) {
                                    geoCallBack.getMarker(markerInfo);
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onKeyExited(String key)
            {
                MarkerInfo markerInfo = geoMarkerMap.get(key);

                if(markerInfo != null)
                {
                    geoMarkerMap.remove(key);
                    if(geoCallBack != null)
                    {
                        geoCallBack.removeMarker(markerInfo);
                    }
                }
                else
                {
                    ConsoleLog.w(TAG, "marker info is null for key : " + key);
                }
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location)
            {
                MarkerInfo markerInfo = geoMarkerMap.get(key);

                if(markerInfo != null)
                {
                    markerInfo.setLoc(new LatLng(location.latitude, location.longitude));
                    geoMarkerMap.put(key, markerInfo);
                    if(geoCallBack != null)
                    {
                        geoCallBack.getMarker(markerInfo);
                    }
                }
                else
                {
                    ConsoleLog.w(TAG, "marker info is null for key : " + key);
                }
            }

            @Override
            public void onGeoQueryReady()
            {
                ConsoleLog.i(TAG, "geo query ready");
            }

            @Override
            public void onGeoQueryError(FirebaseError error)
            {
                ConsoleLog.w(TAG, "Error code is : " + error.getCode());
                ConsoleLog.w(TAG, "Error Message is : " + error.getMessage());
                ConsoleLog.w(TAG, "Error Detail is : " + error.getDetails());
            }
        });
    }

    private static long getIdFromGeoKey(String key)
    {
        String sid = null;
        if(key.contains(GeoUserStr))
        {
            sid = key.replace(GeoUserStr, "");
        }
        else if(key.contains(GeoVehStr))
        {
            sid = key.replace(GeoVehStr, "");
        }

        long id = DataUtil.stringToLong(sid);

        return id;
    }

    public interface GeoCallBack
    {
        void getMarker(MarkerInfo markerInfo);

        void removeMarker(MarkerInfo markerInfo);
    }

}
