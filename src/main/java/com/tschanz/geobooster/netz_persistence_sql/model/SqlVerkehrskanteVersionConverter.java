package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegung;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class SqlVerkehrskanteVersionConverter implements SqlResultsetConverter<VerkehrskanteVersion> {
    public final static String[] ALL_COLS = SqlVersionConverter.ALL_COLS;

    public final Map<Long, List<VerkehrskanteAuspraegung>> vkVkasMap;


    @Override
    @SneakyThrows
    public VerkehrskanteVersion fromResultSet(ResultSet row) {
        var vkEId = SqlVersionConverter.getElementId(row);
        return new VerkehrskanteVersion(
            SqlVersionConverter.getId(row),
            vkEId,
            SqlVersionConverter.getGueltigVon(row),
            SqlVersionConverter.getGueltigBis(row),
            this.getVerwaltungIds(vkEId),
            this.getVmBitmask(vkEId)
        );
    }


    private List<Long> getVerwaltungIds(long vkEId) {
        return this.vkVkasMap.get(vkEId).stream()
            .map(VerkehrskanteAuspraegung::getVerwaltungId)
            .collect(Collectors.toList());
    }


    private byte getVmBitmask(long vkEId) {
        var vmTypes = this.vkVkasMap.get(vkEId).stream()
            .map(VerkehrskanteAuspraegung::getVerkehrsmittelTyp)
            .collect(Collectors.toList());

        return VerkehrsmittelTyp.getBitMask(vmTypes);
    }
}
