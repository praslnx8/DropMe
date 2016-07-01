package com.prasilabs.dropme.backend.io;

import com.google.appengine.api.datastore.GeoPt;

import java.util.Date;

/**
 * Created by prasi on 31/5/16.
 */
public class RideInput
{
    private long id;
    private long userId;
    private String deviceId;
    private long vehicleId;
    private GeoPt sourceLoc;
    private String destLocName;
    private GeoPt destLoc;
    private GeoPt currentLoc;
    private boolean isClosed;
    private int farePerKm;
    private String message;
    private Date expiryDate;
    private Date startDate;
    private Date closedDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public GeoPt getSourceLoc() {
        return sourceLoc;
    }

    public void setSourceLoc(GeoPt sourceLoc) {
        this.sourceLoc = sourceLoc;
    }

    public String getDestLocName() {
        return destLocName;
    }

    public void setDestLocName(String destLocName) {
        this.destLocName = destLocName;
    }

    public GeoPt getDestLoc() {
        return destLoc;
    }

    public void setDestLoc(GeoPt destLoc) {
        this.destLoc = destLoc;
    }

    public GeoPt getCurrentLoc() {
        return currentLoc;
    }

    public void setCurrentLoc(GeoPt currentLoc) {
        this.currentLoc = currentLoc;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public int getFarePerKm() {
        return farePerKm;
    }

    public void setFarePerKm(int farePerKm) {
        this.farePerKm = farePerKm;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
