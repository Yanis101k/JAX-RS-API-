package com.smartcampus.store;

// Import the Sensor model so this store can manage sensor objects
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.smartcampus.models.Sensor;

public class SensorStore {

    // This map stores all sensors in memory.
    // Key = sensor ID
    // Value = Sensor object
    // ConcurrentHashMap is used to reduce concurrency problems in a multi-request environment.
    private static final Map<Integer, Sensor> sensors = new ConcurrentHashMap<>();

    // This counter generates unique IDs for sensors.
    private static final AtomicInteger idCounter = new AtomicInteger(0);

    // Static block runs once when the class is loaded.
    // We use it to add sample sensor data for testing.
    static {
        // Active sensor assigned to room 1
        Sensor sensor1 = new Sensor(
                idCounter.incrementAndGet(), // unique sensor ID
                "Temperature Sensor A", // sensor name
                "Temperature", // sensor type   
                23.7 , 
                1,                           // room ID
                "MAINTENANCE"                        // active
        );

        // Inactive sensor assigned to room 1
        Sensor sensor2 = new Sensor(
                idCounter.incrementAndGet(),
                "Humidity Sensor A",
                "Humidity",
                24.5 , 
                1,
                "Active"
        );

        // Active sensor assigned to room 2
        Sensor sensor3 = new Sensor(
                idCounter.incrementAndGet(),
                "Motion Sensor B",
                "Motion",
                35.1 , 
                2,
                "Active"
        );

        // Store the sample sensors in memory
        sensors.put(sensor1.getId(), sensor1);
        sensors.put(sensor2.getId(), sensor2);
        sensors.put(sensor3.getId(), sensor3);
    }

    // Returns all sensors currently stored in memory
    public static Collection<Sensor> getAllSensors() {
        return sensors.values();
    }

    // Returns sensors filtered by type
    public static List<Sensor> getSensorsByType(String type) {

        // Create a list to store matching sensors
        List<Sensor> matchingSensors = new ArrayList<>();

        // Loop through all stored sensors
        for (Sensor sensor : sensors.values()) {

            // Check that sensor type is not null and matches the requested type
            if (sensor.getType() != null && sensor.getType().equalsIgnoreCase(type)) {
                matchingSensors.add(sensor);
            }
        }

        // Return the filtered list
        return matchingSensors;
    }

        // Adds a new sensor to the store
    public static Sensor addSensor(Sensor sensor) {

        // Generate unique ID
        int newId = idCounter.incrementAndGet();
        sensor.setId(newId);

        // Store sensor
        sensors.put(newId, sensor);

        return sensor;
    }

    // Returns a list of all sensors assigned to a specific room
    public static List<Sensor> getSensorsByRoomId(int roomId) {

        // Create an empty list to store matching sensors
        List<Sensor> roomSensors = new ArrayList<>();

        // Loop through every sensor in the store
        for (Sensor sensor : sensors.values()) {

            // If this sensor belongs to the requested room, add it to the result list
            if (sensor.getRoomId() == roomId) {
                roomSensors.add(sensor);
            }
        }

        // Return all sensors found for that room
        return roomSensors;
    }

    // Checks whether a room still has at least one active sensor assigned to it
    // This method is the key business-rule check for safe room deletion
    public static boolean hasActiveSensorsInRoom(int roomId) {

        // Loop through all sensors
        for (Sensor sensor : sensors.values()) {

            // If a sensor belongs to the room AND is active,
            // the room must not be deleted
            if (sensor.getRoomId() == roomId && sensor.isActive()) {
                return true;
            }
        }

        // No active sensors were found in the room
        return false;
    }

    // Returns a specific sensor by its ID
    // Useful when nested resources need to verify that the parent sensor exists
    public static Sensor getSensorById(int id) {
        return sensors.get(id);
    }
}