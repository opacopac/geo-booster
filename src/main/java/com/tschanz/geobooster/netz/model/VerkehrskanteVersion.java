package com.tschanz.geobooster.netz.model;

import com.tschanz.geobooster.versioning.model.Version;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


@Getter
@RequiredArgsConstructor
public class VerkehrskanteVersion implements Version {
    private final long id;
    private final long elementId;
    private final LocalDate gueltigVon;
    private final LocalDate gueltigBis;
    private final Collection<Long> verwaltungIds;
    private final byte vmTypeBitmask;


    public List<VerkehrsmittelTyp> getVmTypes() {
        return VerkehrsmittelTyp.getVmTypes(this.vmTypeBitmask);
    }


    public boolean hasOneOfVmTypes(List<VerkehrsmittelTyp> vmTypes) {
        return (VerkehrsmittelTyp.getBitMask(vmTypes) & this.vmTypeBitmask) > 0;
    }


    public boolean hasOneOfVerwaltungIds(List<Long> verwaltungIds) {
        for (var verwaltungId: verwaltungIds) {
            if (this.verwaltungIds.contains(verwaltungId)) {
                return true;
            }
        };

        return false;
    }
}
