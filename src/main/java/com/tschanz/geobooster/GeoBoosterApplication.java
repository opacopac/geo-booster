package com.tschanz.geobooster;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class GeoBoosterApplication {
    @SneakyThrows
    public static void main(String[] args) {
        Class.forName("oracle.jdbc.OracleDriver");
        var ctx = SpringApplication.run(GeoBoosterApplication.class, args);
    }
}
