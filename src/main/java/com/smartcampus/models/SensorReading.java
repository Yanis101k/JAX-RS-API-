package com.smartcampus.models;

public class SensorReading {

    // Unique identifier for this reading event.
    // The coursework suggests a UUID string, so we store it as String.
    private String id;

    // The exact time when the reading was captured.
    // Stored as Unix epoch time in milliseconds.
    private long timestamp;

    // The numeric value recorded by the sensor hardware.
    private double value;

    // Empty constructor required by JSON libraries such as Jackson.
    public SensorReading() {
    }

    // Constructor used when creating reading objects in code.
    public SensorReading(String id, long timestamp, double value) {
        this.id = id;
        this.timestamp = timestamp;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
