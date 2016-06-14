package com.prasilabs.gcmData;

/**
 * Created by prasi on 14/6/16.
 */
public class LocationSharePush
{
    private long id;
    private String loceeName;
    private String loceePicture;
    private double lat;
    private double lon;
    private long createdTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoceeName() {
        return loceeName;
    }

    public void setLoceeName(String loceeName) {
        this.loceeName = loceeName;
    }

    public String getLoceePicture() {
        return loceePicture;
    }

    public void setLoceePicture(String loceePicture) {
        this.loceePicture = loceePicture;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }
}
