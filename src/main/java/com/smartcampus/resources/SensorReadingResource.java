package com.smartcampus.resources;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.smartcampus.exceptions.SensorUnavailableException;
import com.smartcampus.models.Sensor;
import com.smartcampus.models.SensorReading;
import com.smartcampus.store.SensorReadingStore;
import com.smartcampus.store.SensorStore;

public class SensorReadingResource {

    // This is the parent sensor ID.
    // It is passed from SensorResource when the sub-resource is created.
    private final int sensorId;

    public SensorReadingResource(int sensorId) {
        this.sensorId = sensorId;
    }

    // Handles:
    // GET /api/v1/sensors/{sensorId}/readings
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReadingsForSensor() {

        // Get all readings that belong to this sensor.
        List<SensorReading> readings = SensorReadingStore.getReadingsBySensorId(sensorId);

        // Return HTTP 200 OK with the list as JSON.
        return Response.ok(readings).build();
    }

    // Handles:
    // POST /api/v1/sensors/{sensorId}/readings
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createReading(SensorReading reading) {
        

        // Validate that the request body exists.
        if (reading == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Reading data is required.\"}")
                    .build();
        }

        Sensor sensor = SensorStore.getSensorById(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // BUSINESS RULE: sensor in maintenance cannot accept readings
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getState())) {
            throw new SensorUnavailableException(
                    "Sensor " + sensorId + " is currently under maintenance and cannot accept readings."
            );
        }

        // We only require the numeric reading value from the client.
        // The server itself will generate the ID, timestamp, and sensorId.
        // So we create a new clean object to avoid trusting client-provided metadata.
        SensorReading newReading = new SensorReading();

        

        // Generate a unique reading event ID.
        newReading.setId(UUID.randomUUID().toString());

        // Attach this reading to the sensor from the URL path.
        newReading.setSensorId(sensorId);

        // Capture the creation time on the server.
        newReading.setTimestamp(System.currentTimeMillis());

        // Copy the measured value from the request body.
        newReading.setValue(reading.getValue());

        
        sensor.setCurrentValue(newReading.getValue());

        // Save the reading in memory.
        SensorReading createdReading = SensorReadingStore.addReading(newReading);

        // Return HTTP 201 Created with the created reading.
        return Response.status(Response.Status.CREATED)
                .entity(createdReading)
                .build();
    }
}