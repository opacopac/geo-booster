package com.tschanz.geobooster.persistence_sql.model;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;


@RequiredArgsConstructor
public class SqlRowCountMapping implements SqlGenericResultsetMapping<Long> {
    private final static String COL_COUNT = "ROW_COUNT";

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
    public Long mapRow(ResultSet row, int rowNumber) {
        return row.getLong(COL_COUNT);
    }
}
