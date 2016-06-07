package com.prasilabs.dropme.modelengines;

import com.firebase.client.FirebaseError;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.api.client.util.ArrayMap;
import com.prasilabs.dropme.backend.dropMeApi.model.RideDetail;
import com.prasilabs.dropme.backend.dropMeApi.model.RideInput;
import com.prasilabs.dropme.backend.dropMeApi.model.VDropMeUser;
import com.prasilabs.dropme.core.CoreApp;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.enums.MarkerType;
import com.prasilabs.dropme.enums.UserOrVehicle;
import com.prasilabs.dropme.managers.RideManager;
import com.prasilabs.dropme.managers.UserManager;
import com.prasilabs.dropme.pojo.MarkerInfo;
import com.prasilabs.dropme.services.firebase.FireBaseConfig;
import com.prasilabs.dropme.services.location.DropMeLocatioListener;
import com.prasilabs.dropme.utils.LocationUtils;
import com.prasilabs.enums.VehicleType;
import com.prasilabs.util.DataUtil;
import com.prasilabs.util.GeoFireKeyGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by prasi on 27/5/16.
 */
public class HomeGeoModelEngine
{
    private static final double RADIUS_IN_KM = 0.5;
    private static final String GeoUserStr = "user";
    private static final String GeoRideStr = "veh";
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
        RideInput rideInput = RideManager.getRideLite(CoreApp.getAppContext());
        if(rideInput == null)
        {
            LatLng latLng = DropMeLocatioListener.getLatLng(CoreApp.getAppContext());
            addMyGeoPt(latLng);
        }
        else
        {
            LatLng latLng = DropMeLocatioListener.getLatLng(CoreApp.getAppContext());
            RideInput rideLite = RideManager.getRideLite(CoreApp.getAppContext());
            if(rideLite != null)
            {
                String userKey = createGeoPtKey(UserManager.getDropMeUser(CoreApp.getAppContext()));
                removePoint(userKey);
                addGeopt(rideLite, latLng);
            }
        }
    }

    public void addRidePoint(RideInput rideInput)
    {
        if(rideInput != null)
        {
            String userKey = createGeoPtKey(UserManager.getDropMeUser(CoreApp.getAppContext()));
            removePoint(userKey);
            addGeopt(rideInput, LocationUtils.convertToLatLng(rideInput.getCurrentLoc()));
        }
    }

    private void addGeopt(RideInput rideLite, LatLng latLng)
    {
        String key = createGeoPtKey(rideLite);
        if(key != null)
        {
            ConsoleLog.i(TAG, "adding vehicle geopt to firebase  " + key);
            FireBaseConfig.getGeoFire().setLocation(key, new GeoLocation(latLng.latitude, latLng.longitude));
        }
    }

    public void removeAllPoints()
    {
        VDropMeUser vDropMeUser = UserManager.getDropMeUser(CoreApp.getAppContext());
        if(vDropMeUser != null)
        {
            String key = createGeoPtKey(vDropMeUser);
            removePoint(key);
        }

        RideInput rideLite = RideManager.getRideLite(CoreApp.getAppContext());
        if(rideLite != null)
        {
            String key = createGeoPtKey(rideLite);
            removePoint(key);
        }
    }

    public List<Long> getAllRides()
    {
        List<Long> rideList = new ArrayList<>();
        for(Map.Entry<String, MarkerInfo> entry : geoMarkerMap.entrySet())
        {
            MarkerInfo markerInfo = entry.getValue();
            if(markerInfo.getUserOrVehicle().equals(UserOrVehicle.Vehicle.name()))
            {
                long id = getIdFromGeoKey(markerInfo.getKey());
                if(id != 0)
                {
                    rideList.add(id);
                }
            }
        }

        return rideList;
    }

    public void removePoint(String key)
    {
        if(key != null)
        {
            FireBaseConfig.getGeoFire().removeLocation(key);
        }
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
                        } else
                        {
                            final long id = getIdFromGeoKey(key);

                            if(id != 0)
                            {
                                if (key.contains(GeoUserStr))
                                {
                                    VDropMeUser vDropMeUser = UserManager.getDropMeUser(CoreApp.getAppContext());
                                    if (vDropMeUser ==null || id != vDropMeUser.getId())
                                    {
                                        MarkerInfo markerInfo = new MarkerInfo();
                                        markerInfo.setKey(key);
                                        markerInfo.setLoc(new LatLng(location.latitude, location.longitude));
                                        markerInfo.setMarkerType(MarkerType.UserMale.name());
                                        markerInfo.setUserOrVehicle(UserOrVehicle.User.name());

                                        geoMarkerMap.put(key, markerInfo);

                                        if (geoCallBack != null) {
                                            geoCallBack.getMarker(markerInfo);
                                        }
                                    }
                                }
                                else if (key.contains(GeoRideStr))
                                {
                                    ConsoleLog.i(TAG, "rider found and id is : " + id);
                                    RideModelEngine.getInstance().getRideDetail(id, new RideModelEngine.RideDetailCallBack() {
                                        @Override
                                        public void getRideDetail(RideDetail rideDetail)
                                        {
                                            if (rideDetail != null)
                                            {
                                                RideInput rideInput = RideManager.getRideLite(CoreApp.getAppContext());
                                                if(rideInput != null && rideInput.getId() != null && Objects.equals(rideInput.getId(), rideDetail.getRideId()))
                                                {
                                                    ConsoleLog.i(TAG, "rider is me :) ");
                                                }
                                                else
                                                {
                                                    MarkerInfo markerInfo = new MarkerInfo();
                                                    markerInfo.setKey(key);
                                                    markerInfo.setLoc(new LatLng(location.latitude, location.longitude));
                                                    if (rideDetail.getVehicleType().equals(VehicleType.Bike.name())) {
                                                        markerInfo.setMarkerType(MarkerType.Bike.name());
                                                    } else {
                                                        markerInfo.setMarkerType(MarkerType.Car.name());
                                                    }
                                                    markerInfo.setUserOrVehicle(UserOrVehicle.Vehicle.name());

                                                    geoMarkerMap.put(key, markerInfo);

                                                    ConsoleLog.i(TAG, "rider is included");
                                                    if (geoCallBack != null)
                                                    {
                                                        ConsoleLog.i(TAG, "rider sent to callback");
                                                        geoCallBack.getMarker(markerInfo);
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                ConsoleLog.w(TAG, "expired or closed rider location. Need to remove");
                                                removePoint(key);
                                            }
                                        }
                                    });
                                }
                            }
                            else
                            {
                                ConsoleLog.w(TAG, "key has invalid id : " + key);
                            }
                        }
                    }

                    @Override
                    public void onKeyExited(String key)
                    {
                        ConsoleLog.i(TAG, "marker exited");
                        MarkerInfo markerInfo = geoMarkerMap.get(key);

                        if (markerInfo != null)
                        {
                            geoMarkerMap.remove(key);
                            if (geoCallBack != null)
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

    public static String createGeoPtKey(VDropMeUser vDropMeUser)
    {
        if(vDropMeUser != null && vDropMeUser.getId() != null) {
            return GeoUserStr + splitter + CoreApp.getDeviceId() + splitter + vDropMeUser.getId();
        }
        return null;
    }

    public static String createGeoPtKey(RideInput rideLite)
    {
        if(rideLite != null && rideLite.getId() != null)
        {
            return GeoFireKeyGenerator.generateRideKey(rideLite.getId());
        }
        return null;
    }

    private static long getIdFromGeoKey(String key)
    {
        String sid = null;
        String[] splittedKey = key.split(splitter);
        if(splittedKey.length > 0)
        {
            String parentKey = splittedKey[0];

            if(parentKey.equals(GeoUserStr) && splittedKey.length > 2)
            {
                sid = splittedKey[2];
            }
            else if(parentKey.equals(GeoRideStr) && splittedKey.length > 1)
            {
                sid = splittedKey[1];
            }
        }

        long id = DataUtil.stringToLong(sid);

        return id;
    }

    public void clearUserData()
    {
        homeGeoModelEngine = null;
        geoMarkerMap.clear();
        homeQuery = null;
    }

    public interface GeoCallBack
    {
        void getMarker(MarkerInfo markerInfo);

        void removeMarker(MarkerInfo markerInfo);
    }



}
