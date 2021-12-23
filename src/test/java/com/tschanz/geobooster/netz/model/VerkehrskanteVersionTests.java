package com.tschanz.geobooster.netz.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class VerkehrskanteVersionTests {
    @Test
    void test_hasOneOfVmTypes_single() {
        var vkVmTypes = Arrays.asList(VerkehrsmittelTyp.BAHN, VerkehrsmittelTyp.BUS);
        var vkV = new VerkehrskanteVersion(null, null, vkVmTypes);

        var result1 = vkV.hasOneOfVmTypes(Collections.singletonList(VerkehrsmittelTyp.BAHN));
        var result2 = vkV.hasOneOfVmTypes(Collections.singletonList(VerkehrsmittelTyp.BUS));
        var result3 = vkV.hasOneOfVmTypes(Collections.singletonList(VerkehrsmittelTyp.SCHIFF));

        assertTrue(result1);
        assertTrue(result2);
        assertFalse(result3);
    }


    @Test
    void test_hasOneOfVmTypes_multi() {
        var vkVmTypes = Arrays.asList(VerkehrsmittelTyp.BAHN, VerkehrsmittelTyp.BUS);
        var vkV = new VerkehrskanteVersion(null, null, vkVmTypes);

        var result1 = vkV.hasOneOfVmTypes(Arrays.asList(VerkehrsmittelTyp.BAHN, VerkehrsmittelTyp.SCHIFF));
        var result2 = vkV.hasOneOfVmTypes(Arrays.asList(VerkehrsmittelTyp.TRAM, VerkehrsmittelTyp.SCHIFF));

        assertTrue(result1);
        assertFalse(result2);
    }
}
