package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.Verwaltung;
import com.tschanz.geobooster.netz.model.VerwaltungVersion;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionInfoConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Map;


public class SqlVerwaltungVersionConverter {
    public final static String[] ALL_COLS = SqlVersionInfoConverter.ALL_COLS;


    @SneakyThrows
    public static VerwaltungVersion fromResultSet(ResultSet row, Map<Long, Verwaltung> verwaltungElementMap) {
        var verwV = new VerwaltungVersion(
            SqlVersionInfoConverter.fromResultSet(row, verwaltungElementMap)
        );
        verwV.getVersionInfo().getElement().getElementInfo().getVersions().add(verwV);

        return verwV;
    }
}
