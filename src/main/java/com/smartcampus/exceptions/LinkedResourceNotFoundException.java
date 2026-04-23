package com.smartcampus.exceptions;

/**
 * Thrown when a referenced resource (like roomId) does not exist.
 */
public class LinkedResourceNotFoundException extends RuntimeException {

    public LinkedResourceNotFoundException(String message) {
        super(message);
    }
}