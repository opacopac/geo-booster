package com.tschanz.geobooster;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class GeoBoosterApplication {
    @SneakyThrows
    public static void main(String[] args) {
        Class.forName("oracle.jdbc.OracleDriver");
        SpringApplication.run(GeoBoosterApplication.class, args);
    }
}
