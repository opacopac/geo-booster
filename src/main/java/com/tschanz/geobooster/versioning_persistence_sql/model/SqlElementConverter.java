package com.tschanz.geobooster.versioning_persistence_sql.model;

import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlElementConverter {
    public final static String COL_ID = "ID";
    public final static String[] ALL_COLS = {COL_ID};


    @SneakyThrows
    public static long getId(ResultSet row) {
        return row.getLong(COL_ID);
    }
}
