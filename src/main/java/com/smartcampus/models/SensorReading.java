package com.smartcampus.models;

public class SensorReading {

    // Unique ID for this reading event.
    // We use String because UUID values are usually stored as text.
    private String id;

    // The sensor that produced this reading.
    // This is useful because each reading belongs to exactly one sensor.
    private int sensorId;

    // The moment when the reading was captured.
    // Stored as epoch milliseconds.
    private long timestamp;

    // The actual numeric value measured by the sensor.
    private double value;

    // Empty constructor required by JSON libraries such as Jackson.
    public SensorReading() {
    }

    // Full constructor used when creating SensorReading objects in Java code.
    public SensorReading(String id, int sensorId, long timestamp, double value) {
        this.id = id;
        this.sensorId = sensorId;
        this.timestamp = timestamp;
        this.value = value;
    }

    // Returns the reading ID.
    public String getId() {
        return id;
    }

    // Sets the reading ID.
    public void setId(String id) {
        this.id = id;
    }

    // Returns the ID of the sensor that owns this reading.
    public int getSensorId() {
        return sensorId;
    }

    // Sets the sensor ID for this reading.
    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    // Returns the timestamp of the reading.
    public long getTimestamp() {
        return timestamp;
    }

    // Sets the timestamp of the reading.
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // Returns the measured value.
    public double getValue() {
        return value;
    }

    // Sets the measured value.
    public void setValue(double value) {
        this.value = value;
    }
}