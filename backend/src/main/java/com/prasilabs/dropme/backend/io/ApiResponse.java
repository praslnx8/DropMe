package com.prasilabs.dropme.backend.io;

/**
 * Created by prasi on 26/5/16.
 */
public class ApiResponse
{
    private long id;
    private boolean status;
    private String message;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
