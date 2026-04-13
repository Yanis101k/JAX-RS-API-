package com.smartcampus;

import org.glassfish.jersey.server.ResourceConfig;


public class SmartCampusApplication extends ResourceConfig {

    public SmartCampusApplication() {
        packages("com.smartcampus.resources");
    }
}