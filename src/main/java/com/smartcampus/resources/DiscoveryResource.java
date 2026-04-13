package com.smartcampus.resources;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApiMetadata() {
        Map<String, Object> body = new LinkedHashMap<>();

        body.put("name", "Smart Campus API");
        body.put("version", "v1");
        body.put("contact", "admin@smartcampus.local");

        Map<String, String> resources = new LinkedHashMap<>();
        resources.put("rooms", "/api/v1/rooms");
        resources.put("sensors", "/api/v1/sensors");

        body.put("resources", resources);

        return Response.ok(body).build();
    }
}
