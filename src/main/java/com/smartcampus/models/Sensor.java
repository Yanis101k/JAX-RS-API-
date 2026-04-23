package com.smartcampus.models;

public class Sensor {

    // Unique identifier for the sensor
    private int id;

    // Human-readable sensor name
    private String name;
    
    // Human-readable sensor type
    private String type; 

    // The latest value measured by this sensor
    private double currentValue;

    // The ID of the room this sensor belongs to
    private int roomId;


    
    // Indicates whether the sensor is currently active
    private String state;

    // Empty constructor required for JSON serialization/deserialization
    public Sensor() {
    }

    // Constructor used when creating sample sensor objects
    public Sensor(int id, String name, String type , double currentValue , int roomId, String state) {
        this.id = id;
        this.name = name;
        this.type = type ;
        this.currentValue = currentValue ;  
        this.roomId = roomId;
        this.state = state;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRoomId() {
        return roomId;
    }
    
    public double getCurrentValue() {
        return currentValue ; 
    }

    public void setCurrentValue( double currentValue ) {
        this.currentValue = currentValue ; 
    }
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isActive() {
        return  getState().equalsIgnoreCase("active")  ;
    }
    
    
}