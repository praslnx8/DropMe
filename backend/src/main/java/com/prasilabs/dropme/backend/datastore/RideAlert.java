package com.prasilabs.dropme.backend.datastore;

import com.google.appengine.api.datastore.GeoPt;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;

/**
 * Created by prasi on 8/6/16.
 */
@Entity
public class RideAlert
{
    public static final String SOURCE_PT = "sourcePt";
    public static final String DEST_PT = "destPt";
    public static final String IS_DELETED_STR = "isDeleted";
    public static final String USER_ID_STR = "userId";
    public static final String CREATED_STR = "created";


    @Id
    private Long id;
    @Index
    private long userId;
    @Index
    private GeoPt sourcePt;
    private String sourceName;
    @Index
    private GeoPt destPt;
    private String destName;
    private Date startTime;
    private Date endTime;
    @Index
    private String gender;
    @Index
    private String vehicleType;
    @Index
    private boolean isDeleted;
    @Index
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

    public GeoPt getSourcePt() {
        return sourcePt;
    }

    public void setSourcePt(GeoPt sourcePt) {
        this.sourcePt = sourcePt;
    }

    public GeoPt getDestPt() {
        return destPt;
    }

    public void setDestPt(GeoPt destPt) {
        this.destPt = destPt;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
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

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }
}
