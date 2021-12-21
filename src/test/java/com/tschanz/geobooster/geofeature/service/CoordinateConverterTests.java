package com.tschanz.geobooster.geofeature.service;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Epsg4326Coordinate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class CoordinateConverterTests {
    @Test
    void test_convertEpsg4326to3857() {
        var lonLat = new Epsg4326Coordinate(7.0, 47.0);

        var result = CoordinateConverter.convertEpsg4326to3857(lonLat);

        assertEquals(779236.44, result.getE(), 0.1);
        assertEquals(5942074.07, result.getN(), 0.1);
    }

    @Test
    void test_convertEpsg3857to4326() {
        var webmercator = new Epsg3857Coordinate(779236.44,5942074.07);

        var result = CoordinateConverter.convertEpsg3857To4326(webmercator);

        assertEquals(7.0, result.getLongitude(), 0.0000001);
        assertEquals(47.0, result.getLatitude(), 0.0000001);
    }
}
