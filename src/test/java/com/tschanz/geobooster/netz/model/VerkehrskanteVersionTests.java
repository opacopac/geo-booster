package com.tschanz.geobooster.netz.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class VerkehrskanteVersionTests {
    @Test
    void test_hasOneOfVmTypes_single() {
        var vkVmTypes = VerkehrsmittelTyp.getBitMask(Arrays.asList(VerkehrsmittelTyp.BAHN, VerkehrsmittelTyp.BUS));
        var vkV = new VerkehrskanteVersion(0, 0, LocalDate.now(), LocalDate.now(), Collections.emptyList(), vkVmTypes);

        var result1 = vkV.hasOneOfVmTypes(Collections.singletonList(VerkehrsmittelTyp.BAHN));
        var result2 = vkV.hasOneOfVmTypes(Collections.singletonList(VerkehrsmittelTyp.BUS));
        var result3 = vkV.hasOneOfVmTypes(Collections.singletonList(VerkehrsmittelTyp.SCHIFF));

        assertTrue(result1);
        assertTrue(result2);
        assertFalse(result3);
    }


    @Test
    void test_hasOneOfVmTypes_multi() {
        var vkVmTypes = VerkehrsmittelTyp.getBitMask(Arrays.asList(VerkehrsmittelTyp.BAHN, VerkehrsmittelTyp.BUS));
        var vkV = new VerkehrskanteVersion(0, 0, LocalDate.now(), LocalDate.now(), Collections.emptyList(), vkVmTypes);

        var result1 = vkV.hasOneOfVmTypes(Arrays.asList(VerkehrsmittelTyp.BAHN, VerkehrsmittelTyp.SCHIFF));
        var result2 = vkV.hasOneOfVmTypes(Arrays.asList(VerkehrsmittelTyp.TRAM, VerkehrsmittelTyp.SCHIFF));

        assertTrue(result1);
        assertFalse(result2);
    }
}
