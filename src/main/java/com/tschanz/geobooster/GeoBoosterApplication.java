package com.tschanz.geobooster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GeoBoosterApplication {

    public static void main(String[] args) {
        var ctx = SpringApplication.run(GeoBoosterApplication.class, args);
    }

}