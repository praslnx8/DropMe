package com.prasilabs.dropme.backend.services.emails.pojo;

/**
 * Created by prasi on 16/3/16.
 */
public class EmailWithName
{
    private String emailAddress;
    private String name;

    public EmailWithName(String emailAddress, String name)
    {
        this.emailAddress = emailAddress;
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
