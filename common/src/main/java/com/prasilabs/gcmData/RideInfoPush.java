package com.prasilabs.gcmData;

/**
 * Created by prasi on 9/6/16.
 */
public class RideInfoPush
{
    private long rideID;
    private String ownerName;
    private String destLocation;
    private String ownerPicture;
    private String vehicleType;
    private String ownerPhone;

    public long getRideID() {
        return rideID;
    }

    public void setRideID(long rideID) {
        this.rideID = rideID;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getDestLocation() {
        return destLocation;
    }

    public void setDestLocation(String destLocation) {
        this.destLocation = destLocation;
    }

    public String getOwnerPicture() {
        return ownerPicture;
    }

    public void setOwnerPicture(String ownerPicture) {
        this.ownerPicture = ownerPicture;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }
}
