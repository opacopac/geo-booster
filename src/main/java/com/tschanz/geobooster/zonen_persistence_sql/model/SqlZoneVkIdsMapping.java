package com.tschanz.geobooster.zonen_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.util.model.Tuple2;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;


@RequiredArgsConstructor
public class SqlZoneVkIdsMapping implements SqlStandardMapping<Tuple2<Long, Long>, SqlLongFilter, Long> {
    private final static String COL_ID_ZONE_VERSION = "ID_ZONE_VERSION";
    private final static String COL_ID_KANTE_ELEMENT = "ID_KANTE_ELEMENT";

    private final Collection<Long> filterVersionIds;

    @Override
    public String getTable() {
        return "Z_ZONE_KANTE_ZUORDNUNG";
    }


    @Override
    public String[] getSelectFields() {
        return new String[] { COL_ID_ZONE_VERSION, COL_ID_KANTE_ELEMENT };
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return SqlLongFilter.createSingleton(COL_ID_ZONE_VERSION, this.filterVersionIds);
    }


    @Override
    @SneakyThrows
    public Tuple2<Long, Long> fromResultSet(ResultSet row) {
        return new Tuple2<>(
            row.getLong(COL_ID_ZONE_VERSION),
            row.getLong(COL_ID_KANTE_ELEMENT)
        );
    }


    @Override
    @SneakyThrows
    public Tuple2<Long, Long> fromJsonAgg(JsonReader reader) {
        return new Tuple2<>(
            reader.nextLong(),
            reader.nextLong()
        );
    }
}
