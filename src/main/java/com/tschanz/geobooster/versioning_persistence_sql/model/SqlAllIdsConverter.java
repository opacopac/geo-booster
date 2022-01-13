package com.tschanz.geobooster.versioning_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlJsonAggConverter;
import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;


@RequiredArgsConstructor
public class SqlAllIdsConverter implements SqlResultsetConverter<Long>, SqlJsonAggConverter<Long> {
    private final String table;


    @Override
    public String getTable() {
        return this.table;
    }


    @Override
    public String[] getSelectFields() {
        return new String[] { SqlHasIdConverter.COL_ID };
    }


    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM %s",
            String.join(",", this.getSelectFields()),
            this.getTable()
        );
    }


    @Override
    @SneakyThrows
    public Long fromResultSet(ResultSet row) {
        return SqlHasIdConverter.getId(row);
    }


    @Override
    @SneakyThrows
    public Long fromJsonAgg(JsonReader reader) {
        return reader.nextLong();
    }
}
