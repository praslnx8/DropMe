package com.prasilabs.dropme.backend.io;

import com.google.appengine.api.datastore.GeoPt;
import com.prasilabs.dropme.backend.datastore.pojo.VNumber;

import java.util.Date;

/**
 * Created by prasi on 31/5/16.
 */
public class RideDetail
{
    private long rideId;
    private GeoPt currentLatLng;
    private GeoPt destLatLng;
    private String destLoc;
    private long ownerId;
    private String ownerName;
    private String ownerPicture;
    private String ownerPhone;
    private String gender;
    private Date startDate;
    private String vehicleType;
    private VNumber vehicleNumber;

    public long getRideId() {
        return rideId;
    }

    public void setRideId(long rideId) {
        this.rideId = rideId;
    }

    public GeoPt getDestLatLng() {
        return destLatLng;
    }

    public void setDestLatLng(GeoPt destLatLng) {
        this.destLatLng = destLatLng;
    }

    public String getDestLoc() {
        return destLoc;
    }

    public void setDestLoc(String destLoc) {
        this.destLoc = destLoc;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerPicture() {
        return ownerPicture;
    }

    public void setOwnerPicture(String ownerPicture) {
        this.ownerPicture = ownerPicture;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public VNumber getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(VNumber vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public GeoPt getCurrentLatLng() {
        return currentLatLng;
    }

    public void setCurrentLatLng(GeoPt currentLatLng) {
        this.currentLatLng = currentLatLng;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }
}
