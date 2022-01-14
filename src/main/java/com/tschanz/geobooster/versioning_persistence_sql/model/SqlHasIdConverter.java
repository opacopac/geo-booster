package com.tschanz.geobooster.versioning_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.stream.Collectors;


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


    public static String getWhereClause(Collection<Long> ids) {
        var idStrings = ids.stream().map(Object::toString).collect(Collectors.toList());
        return String.format(
            " WHERE(%s IN (%s)",
            COL_ID,
            String.join(",", idStrings)
        );
    }
}
