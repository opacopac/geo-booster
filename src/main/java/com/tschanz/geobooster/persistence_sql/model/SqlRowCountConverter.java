package com.tschanz.geobooster.persistence_sql.model;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;


@RequiredArgsConstructor
public class SqlRowCountConverter implements SqlResultsetConverter<Long> {
    private final static String COL_COUNT = "TKV_COUNT";

    private final String table;


    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT COUNT(*) AS %s FROM %s",
            COL_COUNT,
            this.table
        );
    }


    @Override
    @SneakyThrows
    public Long fromResultSet(ResultSet row) {
        return row.getLong(COL_COUNT);
    }
}
