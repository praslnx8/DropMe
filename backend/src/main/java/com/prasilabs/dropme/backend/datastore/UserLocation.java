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
public class UserLocation
{
    public static final String USER_ID_STR = "userId";
    public static final String DEVICE_ID_STR = "deviceId";

    @Id
    private Long id;
    @Index
    private long userId;
    @Index
    private String deviceId;
    @Index
    private GeoPt currentLoc;
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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public GeoPt getCurrentLoc() {
        return currentLoc;
    }

    public void setCurrentLoc(GeoPt currentLoc) {
        this.currentLoc = currentLoc;
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
