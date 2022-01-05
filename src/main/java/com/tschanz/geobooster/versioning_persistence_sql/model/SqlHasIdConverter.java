package com.tschanz.geobooster.versioning_persistence_sql.model;

import com.tschanz.geobooster.netz_persistence_sql.model.SqlResultsetConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlHasIdConverter implements SqlResultsetConverter<Long> {
    public final static String COL_ID = "ID";
    public final static String[] SELECT_COLS = {COL_ID};


    @SneakyThrows
    public static long getId(ResultSet row) {
        return row.getLong(COL_ID);
    }


    @Override
    @SneakyThrows
    public Long fromResultSet(ResultSet row) {
        return row.getLong(COL_ID);
    }
}
