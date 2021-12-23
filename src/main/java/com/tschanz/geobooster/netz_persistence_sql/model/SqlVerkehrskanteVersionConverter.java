package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.*;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionInfoConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class SqlVerkehrskanteVersionConverter {
    public final static String[] ALL_COLS = SqlVersionInfoConverter.ALL_COLS;


    @SneakyThrows
    public static VerkehrskanteVersion fromResultSet(
        ResultSet row,
        Map<Long, Verkehrskante> elementMap,
        Map<Long, List<VerkehrskanteAuspraegung>> vkasByVkEMap,
        Map<Long, Verwaltung> verwaltungMap
    ) {
        var versionInfo = SqlVersionInfoConverter.fromResultSet(row, elementMap);
        var vkEId = versionInfo.getElement().getElementInfo().getId();
        var vkV = new VerkehrskanteVersion(
            versionInfo,
            getVkVerwaltungen(vkasByVkEMap.get(vkEId), verwaltungMap),
            getVkVmTypes(vkasByVkEMap.get(vkEId))
        );
        // add to element's version list
        vkV.getVersionInfo().getElement().getElementInfo().getVersions().add(vkV);

        return vkV;
    }


    private static List<Verwaltung> getVkVerwaltungen(List<VerkehrskanteAuspraegung> vkaList, Map<Long, Verwaltung> verwaltungMap) {
        // TODO: filter by version
        return vkaList.stream()
            .map(vka -> verwaltungMap.get(vka.getVerkehrskante().getElementInfo().getId()))
            .collect(Collectors.toList());
    }


    private static List<VerkehrsmittelTyp> getVkVmTypes(List<VerkehrskanteAuspraegung> vkaList) {
        return vkaList.stream()
            .map(VerkehrskanteAuspraegung::getVerkehrsmittelTyp)
            .collect(Collectors.toList());
    }
}
