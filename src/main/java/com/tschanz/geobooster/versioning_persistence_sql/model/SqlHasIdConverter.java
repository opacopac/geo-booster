package com.tschanz.geobooster.versioning_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlHasIdConverter  {
    public final static String COL_ID = "ID";


    @SneakyThrows
    public static long getId(ResultSet row) {
        return row.getLong(COL_ID);
    }


    @SneakyThrows
    public static long getIdFromJsonAgg(JsonReader reader) {
        return reader.nextLong();
    }
}
