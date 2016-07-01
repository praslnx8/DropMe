package com.prasilabs.dropme.backend.io;

import java.util.Date;

/**
 * Created by prasi on 1/7/16.
 */
public class DropMeUserEditIO {
    private String mobile;
    private String name;
    private String picture;
    private Date dob;
    private String message;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
