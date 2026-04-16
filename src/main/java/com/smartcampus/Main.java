package com.smartcampus;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;


public class Main {

    public static final String BASE_URI = "http://localhost:8080/";

    public static HttpServer startServer() {
        return GrizzlyHttpServerFactory.createHttpServer(
                URI.create(BASE_URI),
                new SmartCampusApplication()
        );
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = startServer();

        System.out.println("Smart Campus API is running.");
        System.out.println("Try: " + BASE_URI );
        System.out.println("Press Ctrl+C to stop.");

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            System.err.println("Server interrupted: " + e.getMessage());
        } finally {
            server.shutdownNow();
        }
    }
}