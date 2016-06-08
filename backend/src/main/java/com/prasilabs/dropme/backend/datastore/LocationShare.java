package com.prasilabs.dropme.backend.datastore;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;

/**
 * Created by prasi on 8/6/16.
 */
@Entity
public class LocationShare
{
    public static final String USER_ID_STR = "userId";
    public static final String DEVICE_ID_STR = "deviceId";
    public static final String RECIEVER_ID_STR = "recieverId";
    public static final String IS_INACTIVE_STR = "isInactive";
    public static final String MODIFIED_TIME_STR = "modifiedTime";

    @Id
    private Long id;
    @Index
    private long userId;
    @Index
    private long recieverId;
    @Index
    private String deviceId;
    @Index
    private boolean isInactive;
    @Index
    private Date createdTime;
    @Index
    private Date modifiedTime;

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

    public long getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(long recieverId) {
        this.recieverId = recieverId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isInactive() {
        return isInactive;
    }

    public void setInactive(boolean inactive) {
        isInactive = inactive;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}
