package com.smartcampus.resources;

// Import the Room model so the API can receive and return Room objects
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.smartcampus.exceptions.RoomNotEmptyException;
import com.smartcampus.models.Room;
import com.smartcampus.store.RoomStore;
import com.smartcampus.store.SensorStore;

// This resource handles all requests related to rooms
// Since the application already uses @ApplicationPath("/api/v1"),
// this resource will be available under /api/v1/rooms
@Path("api/v1/rooms/")
public class RoomResource {

    // Handles GET requests to /api/v1/rooms
    // Returns all rooms currently stored in memory
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Room> getAllRooms() {

        // Ask the RoomStore for all stored rooms
        Collection<Room> rooms = RoomStore.getAllRooms();

        // Return the collection as JSON
        return rooms;
    }

    // Handles POST requests to /api/v1/rooms
    // Accepts a Room object in JSON format and creates a new room in memory
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRoom(Room room) {

        // Simple validation:
        // if the request body is missing, return 400 Bad Request
        if (room == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Room data is required.\"}")
                    .build();
        }

        // Additional simple validation:
        // room name and building should not be null or empty
        if (room.getName() == null || room.getName().trim().isEmpty()
                || room.getBuilding() == null || room.getBuilding().trim().isEmpty()) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Room name and building are required.\"}")
                    .build();
        }

        // Capacity should not be negative
        if (room.getCapacity() < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Capacity must be zero or greater.\"}")
                    .build();
        }

        // Store the new room in memory
        Room createdRoom = RoomStore.addRoom(room);

        // Return HTTP 201 Created with the created room as JSON
        return Response.status(Response.Status.CREATED)
                .entity(createdRoom)
                .build();
    }

    // Handles GET requests to /api/v1/rooms/{id}
    // Returns details of a specific room
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomById(@PathParam("id") int id) {

        // Retrieve the room from the store using the provided ID
        Room room = RoomStore.getRoomById(id);

        // If no room is found, return 404 Not Found
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Room not found.\"}")
                    .build();
        }

        // If the room exists, return it with HTTP 200 OK
        return Response.ok(room).build();
    }

            // Handles DELETE requests to /api/v1/rooms/{id}
        // Deletes a room only if it has no active sensors assigned to it
        @DELETE
        @Path("/{id}")
        @Produces(MediaType.APPLICATION_JSON)
        public Response deleteRoom(@PathParam("id") int id) {

            // First, check whether the room exists
            Room existingRoom = RoomStore.getRoomById(id);

            // If the room does not exist, return 404 Not Found
            if (existingRoom == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Room not found.\"}")
                        .build();
            }

            // Business rule:
            // A room cannot be deleted if it still has active sensors assigned to it
            if (SensorStore.hasActiveSensorsInRoom(id)) {
                throw new RoomNotEmptyException(
                "Room " + id + " cannot be deleted because it contains active sensors."
        );
            }

            // If the room exists and has no active sensors, delete it
            Room deletedRoom = RoomStore.deleteRoom(id);

            // Return HTTP 200 OK with a success message and the deleted room
            return Response.ok()
                    .entity(deletedRoom)
                    .build();
        }
}




