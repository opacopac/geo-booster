package com.tschanz.geobooster.netz_persistence_sql.model;

import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlVkIdsByLinVarIdsConverter implements SqlResultsetConverter<Long> {
    public final static String COL_ID = "VKV_ID";


    @SneakyThrows
    @Override
    public Long fromResultSet(ResultSet row) {
        return row.getLong(COL_ID);
    }
}
