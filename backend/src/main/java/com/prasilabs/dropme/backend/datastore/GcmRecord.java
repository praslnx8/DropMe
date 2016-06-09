package com.prasilabs.dropme.backend.datastore;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;

/**
 * Created by prasi on 5/6/16.
 */
@Entity
public class GcmRecord
{
    public static final String USER_ID_STR = "userId";
    public static final String GCM_ID_STR = "gcmId";
    public static final String DEVICE_ID_STR = "deviceId";
    public static final String IS_DELETED_STR = "isDeleted";

    @Id
    private Long id;
    @Index
    private long userId;
    @Index
    private String gcmId;
    @Index
    private String deviceId;
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

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
