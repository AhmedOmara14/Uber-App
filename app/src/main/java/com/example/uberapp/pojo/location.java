package com.example.uberapp.pojo;

public class location {
    private double Latitude;
    private double Longitude;
    private String type;

    public location() {
    }

    public location(double latitude, double longitude, String type) {
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.type = type;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
