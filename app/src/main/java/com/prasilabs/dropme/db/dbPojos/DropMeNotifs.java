package com.prasilabs.dropme.db.dbPojos;

import com.orm.SugarRecord;

/**
 * Created by prasi on 13/6/16.
 */
public class DropMeNotifs extends SugarRecord
{
    private long id;
    private String jobType;
    private String message;
    private boolean isRead;
    private long createdTime;

    public DropMeNotifs(long id, String jobType, String message) {
        this.id = id;
        this.jobType = jobType;
        this.message = message;
        this.createdTime = System.currentTimeMillis();
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
