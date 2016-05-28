package com.prasilabs.dropme.backend.datastore;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.prasilabs.dropme.backend.datastore.pojo.VNumber;

import java.util.Date;

/**
 * Created by prasi on 27/5/16.
 * take care of vehicle
 */

@Entity
public class Vehicle
{
    @Id
    private Long id;
    private String name;
    @Index
    private String type;
    private String picture;
    private VNumber vNumber;
    @Index
    private long ownerId;
    @Index
    private boolean isDeleted;
    @Index
    private Date created;
    @Index
    private Date modified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public VNumber getvNumber() {
        return vNumber;
    }

    public void setvNumber(VNumber vNumber) {
        this.vNumber = vNumber;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
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
}
