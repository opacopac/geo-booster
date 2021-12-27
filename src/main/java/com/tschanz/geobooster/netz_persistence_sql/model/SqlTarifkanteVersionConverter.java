package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.Tarifkante;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz.model.Verkehrskante;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionInfoConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class SqlTarifkanteVersionConverter {
    public final static String[] ALL_COLS = SqlVersionInfoConverter.ALL_COLS;


    @SneakyThrows
    public static TarifkanteVersion fromResultSet(
        ResultSet row,
        Map<Long, Tarifkante> elementMap,
        Map<Long, List<Long>> tkVkMap,
        Map<Long, Verkehrskante> verkehrskanteMap
    ) {
        var versionInfo = SqlVersionInfoConverter.fromResultSet(row, elementMap);
        var vkIds = tkVkMap.get(versionInfo.getId());
        List<Verkehrskante> vkList = vkIds != null
            ? vkIds.stream().map(verkehrskanteMap::get).collect(Collectors.toList())
            : Collections.emptyList();
        var tkV = new TarifkanteVersion(versionInfo, vkList);

        tkV.getVersionInfo().getElement().getElementInfo().getVersions().add(tkV);

        return tkV;
    }
}
