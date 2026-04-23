package com.smartcampus.exceptions;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Maps SensorUnavailableException to HTTP 403 Forbidden.
 */
@Provider
public class SensorUnavailableExceptionMapper 
        implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException exception) {

        Map<String, String> error = new HashMap<>();
        error.put("error", "Forbidden");
        error.put("message", exception.getMessage());

        return Response.status(Response.Status.FORBIDDEN) // 403
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}