package com.tschanz.geobooster.webmapservice.model;

import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class GetMapRequestViewParamsTests {
    @Test
    void test_mixed1() {
        var viewparams = "TYPEN:'BAHN'\\,'BUS';;;DATE:'2021-12-11';REFRESH_COUNTER:1640119429107;";

        var result = GetMapRequestViewParams.parse(viewparams);

        assertEquals(LocalDate.of(2021, 12, 11), result.getDate());
        assertEquals(1640119429107L, result.getRefreshCounter());
        assertEquals(2, result.getTypes().size());
        assertEquals(VerkehrsmittelTyp.BAHN, result.getTypes().get(0));
        assertEquals(VerkehrsmittelTyp.BUS, result.getTypes().get(1));
    }
}
