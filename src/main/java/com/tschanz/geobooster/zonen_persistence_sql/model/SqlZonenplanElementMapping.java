package com.tschanz.geobooster.zonen_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdMapping;
import com.tschanz.geobooster.zonen.model.Zonenplan;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;


@RequiredArgsConstructor
public class SqlZonenplanElementMapping implements SqlStandardMapping<Zonenplan, SqlLongFilter, Long> {
    private final Collection<Long> elementIds;


    @Override
    public String getTable() {
        return "Z_ZONENPLAN_E";
    }


    @Override
    public String[] getSelectFields() {
        return new String[] { SqlHasIdMapping.COL_ID };
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return SqlLongFilter.createSingleton(SqlHasIdMapping.COL_ID, this.elementIds);
    }


    @Override
    @SneakyThrows
    public Zonenplan mapRow(ResultSet row, int rowNum) {
        return new Zonenplan(
            SqlHasIdMapping.getId(row)
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
