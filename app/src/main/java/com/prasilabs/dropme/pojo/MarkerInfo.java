package com.prasilabs.dropme.pojo;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by prasi on 27/5/16.
 */
public class MarkerInfo
{
    private long id;
    private LatLng loc;
    private String markerType;
    private String userOrVehicle;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LatLng getLoc() {
        return loc;
    }

    public void setLoc(LatLng loc) {
        this.loc = loc;
    }

    public String getMarkerType() {
        return markerType;
    }

    public void setMarkerType(String markerType) {
        this.markerType = markerType;
    }

    public String getUserOrVehicle() {
        return userOrVehicle;
    }

    public void setUserOrVehicle(String userOrVehicle) {
        this.userOrVehicle = userOrVehicle;
    }
}
