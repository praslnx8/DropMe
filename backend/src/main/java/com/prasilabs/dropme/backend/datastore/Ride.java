package com.prasilabs.dropme.backend.datastore;

import com.google.appengine.api.datastore.GeoPt;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;

/**
 * Created by prasi on 30/5/16.
 */
@Entity
public class Ride
{
    @Id
    private Long id;
    //Primary ones
    @Index
    private long userId;
    @Index
    private long deviceId;

    @Index
    private long vehicleId;
    private GeoPt sourceLoc;
    @Index
    private GeoPt destLoc;
    private GeoPt currentLoc;
    private boolean isExpired;
    private boolean isClosed;

    private Date startDate;
    @Index
    private Date endDate;
    @Index
    private Date expiryDate;

    private Date created;
    private Date modified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
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

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }
}
