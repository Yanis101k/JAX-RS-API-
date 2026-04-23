package com.smartcampus.resources;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.smartcampus.exceptions.LinkedResourceNotFoundException;
import com.smartcampus.models.Room;
import com.smartcampus.models.Sensor;
import com.smartcampus.store.RoomStore;
import com.smartcampus.store.SensorStore;

// This resource handles all requests related to sensors
@Path("api/v1/sensors/")
public class SensorResource {

    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Sensor> getAllSensors(@QueryParam("type") String type) {

        // If no type query parameter is provided, return all sensors
        if (type == null || type.trim().isEmpty()) {
            return SensorStore.getAllSensors();
        }

        // If type is provided, return only matching sensors
        return SensorStore.getSensorsByType(type);
    }

    // POST /api/v1/sensors
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSensor(Sensor sensor) {

        // 1. Validate request body
        if (sensor == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Sensor data is required.\"}")
                    .build();
        }

        // 2. Validate required fields (similar style to RoomResource)
        if (sensor.getName() == null || sensor.getName().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Sensor name is required.\"}")
                    .build();
        }

        // 3. BUSINESS RULE:
        // Check if roomId exists
        Room room = RoomStore.getRoomById(sensor.getRoomId());

        if (room == null) {
             throw new LinkedResourceNotFoundException(
                "The specified roomId " + sensor.getRoomId() + " does not exist."
        );
        }

        // 4. Create sensor
        Sensor createdSensor = SensorStore.addSensor(sensor);

        // 5. Return 201 Created
        return Response.status(Response.Status.CREATED)
                .entity(createdSensor)
                .build();
    }

    // Sub-resource locator for sensor readings
    // This handles requests such as /api/v1/sensors/{sensorId}/readings
    // Instead of directly returning data here, it delegates the request
    // to a dedicated SensorReadingResource class.
        @Path("{sensorId}/readings")
        public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") int sensorId) {

            // Validate that the parent sensor exists before handing control
            // to the nested resource. If it does not exist, we stop the request
            // by throwing a 404 Not Found response.
            Sensor sensor = SensorStore.getSensorById(sensorId);

            if (sensor == null) {
                throw new javax.ws.rs.NotFoundException(
                        Response.status(Response.Status.NOT_FOUND)
                                .entity("{\"error\":\"Sensor not found.\"}")
                                .build()
                );
            }

            // Return a new instance of the dedicated sub-resource.
            // Jersey will continue processing the remaining part of the path
            // using that returned object.
            return new SensorReadingResource(sensorId);
        }
}
