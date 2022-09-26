package com.tschanz.geobooster.netz.model;

import com.tschanz.geobooster.versioning.model.Pflegestatus;
import com.tschanz.geobooster.versioning.model.Version;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;


@Getter
@RequiredArgsConstructor
public class VerkehrskanteVersion implements Version {
    private final long id;
    private final long elementId;
    private final LocalDate gueltigVon;
    private final LocalDate gueltigBis;
    private final LocalDate terminiertPer;
    private final Pflegestatus pflegestatus;
    private final Collection<Long> verwaltungIds;
    private final byte vmTypeBitmask;


    public boolean hasOneOfVerwaltungAndVmTypes(Collection<VerkehrsmittelTyp> vmTypes, Map<Long, Long> verwaltungIdMap) {
        if (!this.hasOneOfVmTypes(vmTypes)) {
            return false;
        }

        if (verwaltungIdMap.isEmpty()) {
            return true;
        }

        for (var verwaltungId: this.verwaltungIds) {
            if (verwaltungIdMap.containsKey(verwaltungId)) {
                return true;
            }
        };

        return this.hasOneOfVmTypes(VerkehrsmittelTyp.FUSSWEG_ONLY) && vmTypes.contains(VerkehrsmittelTyp.FUSSWEG);
    }


    private boolean hasOneOfVmTypes(Collection<VerkehrsmittelTyp> vmTypes) {
        return (VerkehrsmittelTyp.getBitMask(vmTypes) & this.vmTypeBitmask) > 0;
    }
}
