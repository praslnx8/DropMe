package com.prasilabs.dropme.backend.io;

/**
 * Created by prasi on 8/6/16.
 */
public class GcmRecordIO
{
    private long userId;
    private String gcmId;
    private String deviceId;

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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
