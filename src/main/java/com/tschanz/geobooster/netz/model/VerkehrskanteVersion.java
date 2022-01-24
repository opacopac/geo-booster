package com.tschanz.geobooster.netz.model;

import com.tschanz.geobooster.versioning.model.Version;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Getter
@RequiredArgsConstructor
public class VerkehrskanteVersion implements Version {
    private final long id;
    private final long elementId;
    private final LocalDate gueltigVon;
    private final LocalDate gueltigBis;
    private final LocalDate terminiertPer;
    private final Collection<Long> verwaltungIds;
    private final byte vmTypeBitmask;


    public List<VerkehrsmittelTyp> getVmTypes() {
        return VerkehrsmittelTyp.getVmTypes(this.vmTypeBitmask);
    }


    public boolean hasOneOfVmTypes(Collection<VerkehrsmittelTyp> vmTypes) {
        if (vmTypes.isEmpty()) {
            return true;
        }

        return (VerkehrsmittelTyp.getBitMask(vmTypes) & this.vmTypeBitmask) > 0;
    }


    public boolean hasOneOfVerwaltungIds(Map<Long, Long> verwaltungIdMap) {
        if (verwaltungIdMap.isEmpty()) {
            return true;
        }

        var fussweg = Collections.singletonList(VerkehrsmittelTyp.FUSSWEG);
        for (var verwaltungId: this.verwaltungIds) {
            if (verwaltungIdMap.containsKey(verwaltungId) || this.hasOneOfVmTypes(fussweg)) {
                return true;
            }
        };

        return false;
    }
}
