package com.smartcampus.exceptions;

/**
 * Thrown when a sensor cannot accept readings (e.g., in MAINTENANCE).
 */
public class SensorUnavailableException extends RuntimeException {

    public SensorUnavailableException(String message) {
        super(message);
    }
}