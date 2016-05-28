package com.prasilabs.dropme.backend.datastore;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;
import java.util.List;

/**
 * Created by prasi on 25/5/16.
 */
@Entity
public class DropMeUser
{
    public static final String EMAIL_STR = "email";
    public static final String HASH_STR = "hash";

    @Id
    private Long id;
    @Index
    private String hash;
    @Index
    private String email;
    private String picture;
    private String name;
    @Index
    private int gender;
    @Index
    private String loginType;
    @Index
    private String mobile;
    @Index
    private boolean isMobileVerified;
    @Index
    private List<String> roles;
    @Index
    private Date created;
    @Index
    private Date lastLogedIn;
    @Index
    private Date modified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean isMobileVerified() {
        return isMobileVerified;
    }

    public void setMobileVerified(boolean mobileVerified) {
        isMobileVerified = mobileVerified;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
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

    public Date getLastLogedIn() {
        return lastLogedIn;
    }

    public void setLastLogedIn(Date lastLogedIn) {
        this.lastLogedIn = lastLogedIn;
    }
}
