package com.tschanz.geobooster.zonen_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.zonen.model.Zonenplan;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;


@RequiredArgsConstructor
public class SqlZonenplanElementConverter implements SqlStandardConverter<Zonenplan, SqlLongFilter, Long> {
    @Override
    public String getTable() {
        return "Z_ZONENPLAN_E";
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
    public Zonenplan fromResultSet(ResultSet row) {
        return new Zonenplan(
            SqlHasIdConverter.getId(row)
        );
    }


    @Override
    @SneakyThrows
    public Zonenplan fromJsonAgg(JsonReader reader) {
        return new Zonenplan(
            reader.nextLong()
        );
    }
}
