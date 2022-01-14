package com.tschanz.geobooster.versioning_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;


@RequiredArgsConstructor
public class SqlAllIdsConverter implements SqlStandardConverter<Long, SqlLongFilter, Long> {
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
    public Collection<SqlLongFilter> getFilters() {
        return Collections.emptyList();
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
