package com.riffpoint.maptask.data.model;

/**
 * Created by Fedchuk Maxim on 2018-10-07.
 * Copyright (c) 2018 Fedchuk Maxim All rights reserved.
 */
public class Location {
    private double latitude;
    private double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
