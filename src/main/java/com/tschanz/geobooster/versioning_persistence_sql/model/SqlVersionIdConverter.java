package com.tschanz.geobooster.versioning_persistence_sql.model;

import com.tschanz.geobooster.netz_persistence_sql.model.SqlResultsetConverter;

import java.sql.ResultSet;


public class SqlVersionIdConverter implements SqlResultsetConverter<Long> {
    public final static String COL_ID = "ID";
    public final static String[] SELECT_COLS = {COL_ID};


    @Override
    public Long fromResultSet(ResultSet row) {
        return SqlVersionConverter.getId(row);
    }
}
