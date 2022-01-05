package com.tschanz.geobooster.netz_persistence_sql.model;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;


@RequiredArgsConstructor
public class SqlDynamicIdConverter implements SqlResultsetConverter<Long> {
    private final String idColName;


    @SneakyThrows
    @Override
    public Long fromResultSet(ResultSet row) {
        return row.getLong(idColName);
    }
}
