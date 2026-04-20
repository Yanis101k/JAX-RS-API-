
package com.smartcampus.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.smartcampus.models.SensorReading;

public class SensorReadingStore {

    // This map groups readings by sensor ID.
    // Key   = sensor ID
    // Value = list of readings that belong to that sensor
    private static final Map<Integer, List<SensorReading>> readingsBySensorId = new ConcurrentHashMap<>();

    // Static block adds a little sample data for testing.
    static {
        addReading(new SensorReading(
                UUID.randomUUID().toString(),
                1,
                System.currentTimeMillis() - 10000,
                22.4
        ));

        addReading(new SensorReading(
                UUID.randomUUID().toString(),
                1,
                System.currentTimeMillis() - 5000,
                22.8
        ));

        addReading(new SensorReading(
                UUID.randomUUID().toString(),
                2,
                System.currentTimeMillis() - 3000,
                45.2
        ));
    }

    // Returns all readings for one specific sensor.
    public static List<SensorReading> getReadingsBySensorId(int sensorId) {

        // If the sensor has no readings yet, return an empty list
        // instead of null. This is safer and cleaner for API code.
        return readingsBySensorId.getOrDefault(sensorId, new ArrayList<>());
    }

    // Adds a new reading to the correct sensor list.
    public static SensorReading addReading(SensorReading reading) {

        // If this sensor does not yet have a list,
        // create one automatically.
        readingsBySensorId
                .computeIfAbsent(reading.getSensorId(), key -> new ArrayList<>())
                .add(reading);

        return reading;
    }
}