package com.tschanz.geobooster.versioning_persistence_sql.model;

import com.tschanz.geobooster.versioning.model.VersionInfo;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Map;


public class SqlVersionInfoConverter {
    public final static String COL_ID = "ID";
    public final static String COL_IDELEMENT = "ID_ELEMENT";
    public final static String COL_GUELTIGVON = "GUELTIG_VON";
    public final static String COL_GUELTIGBIS = "GUELTIG_BIS";
    public final static String[] ALL_COLS = {COL_ID, COL_IDELEMENT, COL_GUELTIGVON, COL_GUELTIGBIS};


    @SneakyThrows
    public static <T> VersionInfo<T> fromResultSet(ResultSet row, Map<Long, T> elementMap) {
        return new VersionInfo<>(
            row.getLong(COL_ID),
            elementMap.get(row.getLong(COL_IDELEMENT)),
            row.getDate(COL_GUELTIGVON).toLocalDate(),
            row.getDate(COL_GUELTIGBIS).toLocalDate()
        );
    }
}
