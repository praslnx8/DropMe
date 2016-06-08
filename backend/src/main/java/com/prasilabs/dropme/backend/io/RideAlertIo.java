package com.prasilabs.dropme.backend.io;

import com.google.appengine.api.datastore.GeoPt;

import java.util.Date;

/**
 * Created by prasi on 8/6/16.
 */
public class RideAlertIo
{
    private long id;
    private GeoPt source;
    private String sourceName;
    private GeoPt dest;
    private String destName;
    private Date startTime;
    private Date endTime;
    private String gender;
    private String vehicleType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GeoPt getSource() {
        return source;
    }

    public void setSource(GeoPt source) {
        this.source = source;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public GeoPt getDest() {
        return dest;
    }

    public void setDest(GeoPt dest) {
        this.dest = dest;
    }

    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}
