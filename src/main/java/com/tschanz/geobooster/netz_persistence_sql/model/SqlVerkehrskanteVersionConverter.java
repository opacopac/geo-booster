package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.Verkehrskante;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionInfoConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Map;


public class SqlVerkehrskanteVersionConverter {
    public final static String[] ALL_COLS = SqlVersionInfoConverter.ALL_COLS;


    @SneakyThrows
    public static VerkehrskanteVersion fromResultSet(ResultSet row, Map<Long, Verkehrskante> elementMap) {
        var vkV = new VerkehrskanteVersion(
            SqlVersionInfoConverter.fromResultSet(row, elementMap)
        );
        vkV.getVersionInfo().getElement().getElementInfo().getVersions().add(vkV);

        return vkV;
    }
}
