package com.tschanz.geobooster.rtm_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardConverter;
import com.tschanz.geobooster.util.model.KeyValue;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;


@RequiredArgsConstructor
public class SqlRgKorridorTkIdsConverter implements SqlStandardConverter<KeyValue<Long, Long>, SqlLongFilter, Long> {
    private final static String COL_ID_RELATIONSKORRIDOR_V = "ID_RELATIONSKORRIDOR_V";
    private final static String COL_ID_TARIFKANTE_E = "ID_TARIFKANTE_E";


    @Override
    public String getTable() {
        return "R_REL_KORR_X_TARIFKANTE_E";
    }


    @Override
    public String[] getSelectFields() {
        return new String[] { COL_ID_RELATIONSKORRIDOR_V, COL_ID_TARIFKANTE_E };
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return Collections.emptyList();
    }


    @Override
    @SneakyThrows
    public KeyValue<Long, Long> fromResultSet(ResultSet row) {
        return new KeyValue<>(
            row.getLong(COL_ID_RELATIONSKORRIDOR_V),
            row.getLong(COL_ID_TARIFKANTE_E)
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
