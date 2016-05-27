package com.prasilabs.dropme.backend.datastore;

import com.googlecode.objectify.annotation.Entity;
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
    @Index
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
}
