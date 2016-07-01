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
    public static final String USER_ID_STR = "userId";
    public static final String DEVICE_ID_STR = "deviceId";
    public static final String EXPIRY_DATE_STR = "expiryDate";
    public static final String IS_CLOSED_STR = "isClosed";
    public static final String IS_GEO_REMOVED_STR = "isGeoRemoved";
    public static final String DEST_LOC_STR = "destLoc";
    public static final String CREATED_STR = "created";

    @Id
    private Long id;
    //Primary ones
    @Index
    private long userId;
    @Index
    private String deviceId;

    private long vehicleId;
    private GeoPt sourceLoc;
    private String sourceLocName;
    private String destLocName;
    @Index
    private GeoPt destLoc;
    private GeoPt currentLoc;
    @Index
    private boolean isClosed;
    private int farePerKm;
    private Date startDate;
    @Index
    private Date closedDate;
    @Index
    private Date expiryDate;
    @Index
    private boolean isGeoRemoved;

    @Index
    private Date created;
    private Date modified;

    private String message;

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

    public int getFarePerKm() {
        return farePerKm;
    }

    public void setFarePerKm(int farePerKm) {
        this.farePerKm = farePerKm;
    }

    public String getDestLocName() {
        return destLocName;
    }

    public void setDestLocName(String destLocName) {
        this.destLocName = destLocName;
    }

    public boolean isGeoRemoved() {
        return isGeoRemoved;
    }

    public void setGeoRemoved(boolean geoRemoved) {
        isGeoRemoved = geoRemoved;
    }

    public String getSourceLocName() {
        return sourceLocName;
    }

    public void setSourceLocName(String sourceLocName) {
        this.sourceLocName = sourceLocName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
