package com.tschanz.geobooster.webmapservice.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class GetMapViewParamsTests {
    @Test
    void test_mixed1() {
        var viewparams = "TYPEN:'BAHN,BUS';;;DATE:'2021-12-12';REFRESH_COUNTER:1639825696749;";

        var result = GetMapViewParams.parse(viewparams);

        assertEquals(LocalDate.of(2021, 12, 12), result.getDate());
        assertEquals(1639825696749L, result.getRefreshCounter());
        assertEquals(2, result.getTypes().size());
        assertEquals("BAHN", result.getTypes().get(0));
        assertEquals("BUS", result.getTypes().get(1));
    }
}

