package com.prasilabs.dropme.backend.io;

import com.google.appengine.api.datastore.GeoPt;

import java.util.Date;

/**
 * Created by prasi on 10/6/16.
 */
public class MyRideInfo
{
    private long id;
    private VVehicle vehicle;
    private GeoPt sourceLoc;
    private GeoPt destLoc;
    private String sourceLocName;
    private String destLocName;
    private int farePerKm;
    private boolean isCurrent;
    private Date date;
    private String message;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public VVehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(VVehicle vehicle) {
        this.vehicle = vehicle;
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

    public String getDestLocName() {
        return destLocName;
    }

    public void setDestLocName(String destLocName) {
        this.destLocName = destLocName;
    }

    public int getFarePerKm() {
        return farePerKm;
    }

    public void setFarePerKm(int farePerKm) {
        this.farePerKm = farePerKm;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
