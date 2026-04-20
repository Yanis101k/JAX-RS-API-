package com.smartcampus.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.smartcampus.models.SensorReading;

public class SensorReadingResource {

    // The parent sensor ID is injected through the constructor
    // by the sub-resource locator in SensorResource.
    private final int sensorId;

    public SensorReadingResource(int sensorId) {
        this.sensorId = sensorId;
    }

    // Handles GET /api/v1/sensors/{sensorId}/readings
    // Returns sample readings for the selected sensor.
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReadingsForSensor() {

        // In this coursework version, readings are generated in memory.
        // In a real system, they would usually come from a database,
        // message queue, or time-series storage.
        List<SensorReading> readings = new ArrayList<>();

        // Capture the current time once so all sample readings are relative to it.
        long now = System.currentTimeMillis();

        // Create a few sample readings for the requested sensor.
        readings.add(new SensorReading(UUID.randomUUID().toString(), now - 10000, 21.4));
        readings.add(new SensorReading(UUID.randomUUID().toString(), now - 5000, 21.8));
        readings.add(new SensorReading(UUID.randomUUID().toString(), now, 22.1));

        // Return a small JSON object that includes both the sensor ID
        // and the readings collection for clarity.
        return Response.ok(new Object() {
            public final int sensorId = SensorReadingResource.this.sensorId;
            public final List<SensorReading> items = readings;
        }).build();
    }
}