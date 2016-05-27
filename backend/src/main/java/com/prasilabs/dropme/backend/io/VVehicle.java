package com.prasilabs.dropme.backend.io;

import com.prasilabs.dropme.backend.datastore.pojo.VNumber;

/**
 * Created by prasi on 28/5/16.
 */
public class VVehicle
{
    private long id;
    private String name;
    private String type;
    private String picture;
    private VNumber vNumber;
    private long ownerId;
    private boolean isDeleted;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
}
