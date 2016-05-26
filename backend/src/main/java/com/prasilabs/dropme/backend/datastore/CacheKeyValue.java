package com.prasilabs.dropme.backend.datastore;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by prasi on 4/4/16.
 */
@Entity
public class CacheKeyValue implements Serializable
{
    public static final String LAST_ACCESED_STR = "lastAccesed";
    public static final String PARENT_KEY_STR = "parentKey";
    public static final String CACHE_KEY_STR = "cacheKey";

    @Id
    private Long id;
    @Index
    private int parentKey; //parent key to delete or get the list of cahce of particular data
    @Index
    private String cacheKey;
    @Index
    private Date lastAccesed;

    @Index
    private Date created;

    @Index
    private Date modified;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getParentKey() {
        return parentKey;
    }

    public void setParentKey(int parentKey) {
        this.parentKey = parentKey;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public Date getLastAccesed() {
        return lastAccesed;
    }

    public void setLastAccesed(Date lastAccesed) {
        this.lastAccesed = lastAccesed;
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
