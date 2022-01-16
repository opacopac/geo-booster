package com.tschanz.geobooster.versioning_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;


@RequiredArgsConstructor
public class SqlAllIdsMapping implements SqlStandardMapping<Long, SqlLongFilter, Long> {
    private final String table;


    @Override
    public String getTable() {
        return this.table;
    }


    @Override
    public String[] getSelectFields() {
        return new String[] { SqlHasIdMapping.COL_ID };
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return Collections.emptyList();
    }


    @Override
    @SneakyThrows
    public Long fromResultSet(ResultSet row) {
        return SqlHasIdMapping.getId(row);
    }


    @Override
    @SneakyThrows
    public Long fromJsonAgg(JsonReader reader) {
        return reader.nextLong();
    }
}
