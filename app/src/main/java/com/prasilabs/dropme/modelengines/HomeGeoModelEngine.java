package com.prasilabs.dropme.modelengines;

import com.firebase.client.FirebaseError;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.api.client.util.ArrayMap;
import com.prasilabs.dropme.backend.dropMeApi.model.VDropMeUser;
import com.prasilabs.dropme.backend.dropMeApi.model.VVehicle;
import com.prasilabs.dropme.core.CoreApp;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.enums.MarkerType;
import com.prasilabs.dropme.enums.RideStatus;
import com.prasilabs.dropme.enums.UserOrVehicle;
import com.prasilabs.dropme.managers.RideManager;
import com.prasilabs.dropme.managers.UserManager;
import com.prasilabs.dropme.pojo.MarkerInfo;
import com.prasilabs.dropme.services.firebase.FireBaseConfig;
import com.prasilabs.dropme.services.location.DropMeLocatioListener;
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
    private static final String splitter = "-";

    private static final String TAG = HomeGeoModelEngine.class.getSimpleName();

    private static HomeGeoModelEngine homeGeoModelEngine;
    private Map<String,MarkerInfo> geoMarkerMap = new ArrayMap<>();
    private GeoQuery homeQuery;

    public static HomeGeoModelEngine getInstance()
    {
        if(homeGeoModelEngine == null)
        {
            homeGeoModelEngine = new HomeGeoModelEngine();
        }

        return homeGeoModelEngine;
    }

    private HomeGeoModelEngine(){}

    public void addMyGeoPt(LatLng latLng)
    {
        VDropMeUser vDropMeUser = UserManager.getDropMeUser(CoreApp.getAppContext());
        if(vDropMeUser != null)
        {
            String key = createGeoPtKey(vDropMeUser);
            FireBaseConfig.getGeoFire().setLocation(key, new GeoLocation(latLng.latitude, latLng.longitude));
        }
    }

    public void locationChanged()
    {
        ConsoleLog.i(TAG, "location changed");
        if(RideManager.rideStatus == RideStatus.User || RideManager.rideStatus == RideStatus.Riding)
        {
            LatLng latLng = DropMeLocatioListener.getLatLng(CoreApp.getAppContext());
            addMyGeoPt(latLng);
        }
        else if(RideManager.rideStatus == RideStatus.Offering)
        {
            LatLng latLng = DropMeLocatioListener.getLatLng(CoreApp.getAppContext());
            VVehicle vVehicle = RideManager.getCurentOfferingVehicle();
            if(vVehicle != null)
            {
                addGeopt(vVehicle, latLng);
            }
        }
    }

    private void addGeopt(VVehicle vVehicle, LatLng latLng)
    {
        String key = createGeoPtKey(vVehicle);
        FireBaseConfig.getGeoFire().setLocation(key, new GeoLocation(latLng.latitude, latLng.longitude));
    }

    public void removeAllPoints()
    {
        VDropMeUser vDropMeUser = UserManager.getDropMeUser(CoreApp.getAppContext());
        if(vDropMeUser != null)
        {
            String key = createGeoPtKey(vDropMeUser);
            removePoint(key);
        }

        VVehicle vVehicle = RideManager.getCurentOfferingVehicle();
        if(vVehicle != null)
        {
            String key = createGeoPtKey(vVehicle);
            removePoint(key);
        }
    }

    public void removePoint(String key)
    {
        FireBaseConfig.getGeoFire().removeLocation(key);
    }

    public void listenToHomeGeoLoc(LatLng latLng, final GeoCallBack geoCallBack)
    {
        if(latLng != null)
        {
            if (homeQuery == null)
            {
                ConsoleLog.i(TAG, "registering geofire for location");
                homeQuery = FireBaseConfig.getGeoFire().queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), RADIUS_IN_KM);

                homeQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                    @Override
                    public void onKeyEntered(final String key, final GeoLocation location)
                    {
                        ConsoleLog.i(TAG, "marker entered");
                        MarkerInfo existingMarkerInfo = geoMarkerMap.get(key);
                        if (existingMarkerInfo != null) {
                            existingMarkerInfo.setLoc(new LatLng(location.latitude, location.longitude));
                            if (geoCallBack != null) {
                                geoCallBack.getMarker(existingMarkerInfo);
                            }
                        } else {
                            final long id = getIdFromGeoKey(key);

                            if (key.contains(GeoUserStr)) {
                                if (id != UserManager.getDropMeUser(CoreApp.getAppContext()).getId()) {
                                    MarkerInfo markerInfo = new MarkerInfo();
                                    markerInfo.setKey(key);
                                    markerInfo.setLoc(new LatLng(location.latitude, location.longitude));
                                    markerInfo.setMarkerType(MarkerType.User.name());
                                    markerInfo.setUserOrVehicle(UserOrVehicle.User.name());

                                    geoMarkerMap.put(key, markerInfo);

                                    if (geoCallBack != null) {
                                        geoCallBack.getMarker(markerInfo);
                                    }
                                }
                            } else if (key.contains(GeoVehStr)) {
                                VehicleModelEngine.getInstance().getVehicleDetail(id, new VehicleModelEngine.VehicleGetCallBack() {
                                    @Override
                                    public void getVehicle(VVehicle vVehicle) {
                                        MarkerInfo markerInfo = new MarkerInfo();
                                        markerInfo.setKey(key);
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
                        ConsoleLog.i(TAG, "marker exited");
                        MarkerInfo markerInfo = geoMarkerMap.get(key);

                        if (markerInfo != null) {
                            geoMarkerMap.remove(key);
                            if (geoCallBack != null) {
                                geoCallBack.removeMarker(markerInfo);
                            }
                        } else {
                            ConsoleLog.w(TAG, "marker info is null for key : " + key);
                        }
                    }

                    @Override
                    public void onKeyMoved(String key, GeoLocation location)
                    {
                        ConsoleLog.i(TAG, "marker moved");
                        MarkerInfo markerInfo = geoMarkerMap.get(key);

                        if (markerInfo != null) {
                            markerInfo.setLoc(new LatLng(location.latitude, location.longitude));
                            geoMarkerMap.put(key, markerInfo);
                            if (geoCallBack != null) {
                                geoCallBack.getMarker(markerInfo);
                            }
                        } else {
                            ConsoleLog.w(TAG, "marker info is null for key : " + key);
                        }
                    }

                    @Override
                    public void onGeoQueryReady() {
                        ConsoleLog.i(TAG, "geo query ready");
                    }

                    @Override
                    public void onGeoQueryError(FirebaseError error) {
                        ConsoleLog.w(TAG, "Error code is : " + error.getCode());
                        ConsoleLog.w(TAG, "Error Message is : " + error.getMessage());
                        ConsoleLog.w(TAG, "Error Detail is : " + error.getDetails());
                    }
                });
            }
            else
            {
                ConsoleLog.i(TAG, "registering geofire for location");
                homeQuery.setCenter(new GeoLocation(latLng.latitude, latLng.longitude));
            }


        }
    }

    private static String createGeoPtKey(VDropMeUser vDropMeUser)
    {
        return GeoUserStr + splitter + CoreApp.getDeviceId() + splitter + vDropMeUser.getId();
    }

    private static String createGeoPtKey(VVehicle vVehicle)
    {
        return GeoVehStr + splitter + CoreApp.getDeviceId() + splitter + vVehicle.getId();
    }

    private static long getIdFromGeoKey(String key)
    {
        String sid = null;
        String[] splittedKey = key.split("$");
        if(splittedKey.length == 3)
        {
            sid = splittedKey[2];
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
