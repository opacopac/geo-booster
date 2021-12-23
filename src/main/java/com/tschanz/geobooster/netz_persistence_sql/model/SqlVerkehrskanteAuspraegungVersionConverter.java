package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegung;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegungVersion;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionInfoConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Map;


public class SqlVerkehrskanteAuspraegungVersionConverter {
    public final static String[] ALL_COLS = SqlVersionInfoConverter.ALL_COLS;


    @SneakyThrows
    public static VerkehrskanteAuspraegungVersion fromResultSet(ResultSet row, Map<Long, VerkehrskanteAuspraegung> elementMap) {
        var vkV = new VerkehrskanteAuspraegungVersion(
            SqlVersionInfoConverter.fromResultSet(row, elementMap)
        );
        // add to element's version list
        vkV.getVersionInfo().getElement().getElementInfo().getVersions().add(vkV);

        return vkV;
    }
}
