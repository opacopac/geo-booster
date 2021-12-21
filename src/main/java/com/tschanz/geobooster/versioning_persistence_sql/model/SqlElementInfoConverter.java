package com.tschanz.geobooster.versioning_persistence_sql.model;

import com.tschanz.geobooster.versioning.model.ElementInfo;
import com.tschanz.geobooster.versioning.model.Version;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlElementInfoConverter {
    public final static String COL_ID = "ID";
    public final static String[] ALL_COLS = {COL_ID};


    @SneakyThrows
    public static <T, K extends Version<T>> ElementInfo<T, K> fromResultSet(ResultSet row) {
        return new ElementInfo<>(
            row.getLong(COL_ID)
        );
    }
}
