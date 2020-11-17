package com.suchalak.model;

import com.google.gson.annotations.SerializedName;

public class UserPickupsPojo {
    @SerializedName("id")
    private String id;

    @SerializedName("pick_from")
    private String pick_from;

    @SerializedName("pick_to")
    private String pick_to;

    @SerializedName("uname")
    private String uname;

    @SerializedName("user_lat")
    private String user_lat;

    @SerializedName("user_lng")
    private String user_lng;

    @SerializedName("driver_uname")
    private String driver_uname;

    @SerializedName("status")
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPick_from() {
        return pick_from;
    }

    public void setPick_from(String pick_from) {
        this.pick_from = pick_from;
    }

    public String getPick_to() {
        return pick_to;
    }

    public void setPick_to(String pick_to) {
        this.pick_to = pick_to;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUser_lat() {
        return user_lat;
    }

    public void setUser_lat(String user_lat) {
        this.user_lat = user_lat;
    }

    public String getUser_lng() {
        return user_lng;
    }

    public void setUser_lng(String user_lng) {
        this.user_lng = user_lng;
    }

    public String getDriver_uname() {
        return driver_uname;
    }

    public void setDriver_uname(String driver_uname) {
        this.driver_uname = driver_uname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
