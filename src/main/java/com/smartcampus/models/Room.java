package com.smartcampus.models;

public class Room {

    private int id;
    private String name;
    private String building;
    private int capacity;

    public Room() {
    }

    public Room(int id, String name, String building, int capacity) {
        this.id = id;
        this.name = name;
        this.building = building;
        this.capacity = capacity;
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

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
