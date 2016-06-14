package com.prasilabs.dropme.db.dbPojos;

import za.co.cporm.model.CPDefaultRecord;
import za.co.cporm.model.annotation.Column.Column;
import za.co.cporm.model.annotation.Column.PrimaryKey;
import za.co.cporm.model.annotation.Table;

/**
 * Created by prasi on 13/6/16.
 */
@Table
public class DropMeNotifs extends CPDefaultRecord<DropMeNotifs>
{
    public DropMeNotifs(long id, String jobType, String message)
    {
        this.id = id;
        this.jobType = jobType;
        this.message = message;
        this.createdTime = System.currentTimeMillis();
    }

    @PrimaryKey
    private long id;
    @Column(required = true)
    private String jobType;
    @Column
    private String message;
    @Column
    private boolean isRead;
    @Column
    private long createdTime;

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
