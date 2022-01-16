package com.tschanz.geobooster.zonen_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.util.model.KeyValue;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;


@RequiredArgsConstructor
public class SqlZoneVkIdsMapping implements SqlStandardMapping<KeyValue<Long, Long>, SqlLongFilter, Long> {
    private final static String COL_ID_ZONE_VERSION = "ID_ZONE_VERSION";
    private final static String COL_ID_KANTE_ELEMENT = "ID_KANTE_ELEMENT";


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
        return Collections.emptyList();
    }


    @Override
    @SneakyThrows
    public KeyValue<Long, Long> fromResultSet(ResultSet row) {
        return new KeyValue<>(
            row.getLong(COL_ID_ZONE_VERSION),
            row.getLong(COL_ID_KANTE_ELEMENT)
        );
    }


    @Override
    @SneakyThrows
    public KeyValue<Long, Long> fromJsonAgg(JsonReader reader) {
        return new KeyValue<>(
            reader.nextLong(),
            reader.nextLong()
        );
    }
}
