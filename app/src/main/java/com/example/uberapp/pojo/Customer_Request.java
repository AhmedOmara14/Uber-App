package com.example.uberapp.pojo;

public class Customer_Request {
    private String id;
    private double Latitude_currentdriver;
    private double Latitude_currentlocation;
    private double Longitude_currentdriver;
    private double Longitude_currentlocation;

    public Customer_Request() {
    }

    public Customer_Request(String id, double latitude_currentdriver, double latitude_currentlocation, double longitude_currentdriver, double longitude_currentlocation) {
        this.id = id;
        Latitude_currentdriver = latitude_currentdriver;
        Latitude_currentlocation = latitude_currentlocation;
        Longitude_currentdriver = longitude_currentdriver;
        Longitude_currentlocation = longitude_currentlocation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude_currentdriver() {
        return Latitude_currentdriver;
    }

    public void setLatitude_currentdriver(double latitude_currentdriver) {
        Latitude_currentdriver = latitude_currentdriver;
    }

    public double getLatitude_currentlocation() {
        return Latitude_currentlocation;
    }

    public void setLatitude_currentlocation(double latitude_currentlocation) {
        Latitude_currentlocation = latitude_currentlocation;
    }

    public double getLongitude_currentdriver() {
        return Longitude_currentdriver;
    }

    public void setLongitude_currentdriver(double longitude_currentdriver) {
        Longitude_currentdriver = longitude_currentdriver;
    }

    public double getLongitude_currentlocation() {
        return Longitude_currentlocation;
    }

    public void setLongitude_currentlocation(double longitude_currentlocation) {
        Longitude_currentlocation = longitude_currentlocation;
    }
}
