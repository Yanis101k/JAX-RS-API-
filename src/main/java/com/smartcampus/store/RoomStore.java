package com.smartcampus.store;

// Import the Room model class which represents a room object
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.smartcampus.models.Room;

public class RoomStore {

    // This map stores all rooms in memory.
    // Key = Room ID (Integer)
    // Value = Room object
    // ConcurrentHashMap ensures thread safety when multiple users access the API at the same time.
    private static final Map<Integer, Room> rooms = new ConcurrentHashMap<>();

    // This counter is used to generate unique IDs for each new room.
    // AtomicInteger ensures that ID generation is safe even with multiple concurrent requests.
    private static final AtomicInteger idCounter = new AtomicInteger(0);

    // Static block runs once when the class is loaded.
    // We use it to add some initial sample data to the system.
    static {
        // Create first sample room
        Room room1 = new Room(
                idCounter.incrementAndGet(), // generate unique ID
                "Lab A",                    // room name
                "North Block",              // building
                30                          // capacity
        );

        // Create second sample room
        Room room2 = new Room(
                idCounter.incrementAndGet(),
                "Lecture Hall 1",
                "Main Building",
                120
        );

        // Add rooms to the map
        rooms.put(room1.getId(), room1);
        rooms.put(room2.getId(), room2);
    }

    // Returns all rooms stored in memory
    // Used for GET /rooms
    public static Collection<Room> getAllRooms() {
        return rooms.values(); // returns only the values (Room objects)
    }

    // Returns a specific room by its ID
    // Used for GET /rooms/{id}
    public static Room getRoomById(int id) {
        return rooms.get(id); // returns null if not found
    }

    // Adds a new room to the system
    // Used for POST /rooms
    public static Room addRoom(Room room) {

        // Generate a new unique ID for the room
        int newId = idCounter.incrementAndGet();

        // Set the generated ID to the room object
        room.setId(newId);

        // Store the room in the map
        rooms.put(newId, room);

        // Return the created room (useful for API response)
        return room;
    }
}
