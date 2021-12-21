package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.Tarifkante;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionInfoConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Map;


public class SqlTarifkanteVersionConverter {
    public final static String[] ALL_COLS = SqlVersionInfoConverter.ALL_COLS;


    @SneakyThrows
    public static TarifkanteVersion fromResultSet(ResultSet row, Map<Long, Tarifkante> elementMap) {
        var vkV = new TarifkanteVersion(
            SqlVersionInfoConverter.fromResultSet(row, elementMap)
        );
        vkV.getVersionInfo().getElement().getElementInfo().getVersions().add(vkV);

        return vkV;
    }
}
