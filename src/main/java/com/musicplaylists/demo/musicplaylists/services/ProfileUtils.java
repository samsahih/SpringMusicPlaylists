package com.musicplaylists.demo.musicplaylists.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ProfileUtils {

    private final Environment environment;

    @Autowired
    public ProfileUtils(Environment environment) {
        this.environment = environment;
    }

    public boolean isLocalProfile() {
        return Arrays.asList(environment.getActiveProfiles()).contains("local");
    }

    public boolean isProductionProfile() {
        return Arrays.asList(environment.getActiveProfiles()).contains("production");
    }
}