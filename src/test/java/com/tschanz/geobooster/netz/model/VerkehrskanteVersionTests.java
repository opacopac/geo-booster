package com.tschanz.geobooster.netz.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class VerkehrskanteVersionTests {
    @Test
    void test_hasOneOfVmTypes_single() {
        var vkVmTypes = VerkehrsmittelTyp.getBitMask(Arrays.asList(VerkehrsmittelTyp.BAHN, VerkehrsmittelTyp.BUS));
        var vkV = new VerkehrskanteVersion(0, 0, LocalDate.now(), LocalDate.now(), null, Collections.emptyList(), vkVmTypes);

        var empty = new HashMap<Long, Long>();
        var result1 = vkV.hasOneOfVerwaltungAndVmTypes(Collections.singletonList(VerkehrsmittelTyp.BAHN), empty);
        var result2 = vkV.hasOneOfVerwaltungAndVmTypes(Collections.singletonList(VerkehrsmittelTyp.BUS), empty);
        var result3 = vkV.hasOneOfVerwaltungAndVmTypes(Collections.singletonList(VerkehrsmittelTyp.SCHIFF), empty);

        assertTrue(result1);
        assertTrue(result2);
        assertFalse(result3);
    }


    @Test
    void test_hasOneOfVmTypes_multi() {
        var vkVmTypes = VerkehrsmittelTyp.getBitMask(Arrays.asList(VerkehrsmittelTyp.BAHN, VerkehrsmittelTyp.BUS));
        var vkV = new VerkehrskanteVersion(0, 0, LocalDate.now(), LocalDate.now(), null, Collections.emptyList(), vkVmTypes);

        var empty = new HashMap<Long, Long>();
        var result1 = vkV.hasOneOfVerwaltungAndVmTypes(Arrays.asList(VerkehrsmittelTyp.BAHN, VerkehrsmittelTyp.SCHIFF), empty);
        var result2 = vkV.hasOneOfVerwaltungAndVmTypes(Arrays.asList(VerkehrsmittelTyp.TRAM, VerkehrsmittelTyp.SCHIFF), empty);

        assertTrue(result1);
        assertFalse(result2);
    }
}
