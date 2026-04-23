package com.smartcampus.exceptions;

/**
 * Custom exception thrown when trying to delete a room
 * that still contains active sensors.
 */
public class RoomNotEmptyException extends RuntimeException {

    public RoomNotEmptyException(String message) {
        super(message);
    }
}