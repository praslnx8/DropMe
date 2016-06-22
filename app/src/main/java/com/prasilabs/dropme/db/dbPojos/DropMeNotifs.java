package com.prasilabs.dropme.db.dbPojos;

import com.prasilabs.dropme.annotions.DB;

/**
 * Created by prasi on 13/6/16.
 */
public class DropMeNotifs
{
    @DB
    private long id;
    @DB
    private String jobType;
    @DB
    private String message;
    @DB
    private boolean isRead;
    @DB
    private long createdTime;

    public DropMeNotifs() {
    }

    public DropMeNotifs(long id, String jobType, String message) {
        this.id = id;
        this.jobType = jobType;
        this.message = message;
        this.createdTime = System.currentTimeMillis();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }
}
