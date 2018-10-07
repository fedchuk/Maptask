package com.riffpoint.maptask.data.model;

/**
 * Created by Fedchuk Maxim on 2018-10-07.
 * Copyright (c) 2018 Fedchuk Maxim All rights reserved.
 */
public class Items {
    private int id;
    private String name;
    private Location location;

    public Items(int id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
