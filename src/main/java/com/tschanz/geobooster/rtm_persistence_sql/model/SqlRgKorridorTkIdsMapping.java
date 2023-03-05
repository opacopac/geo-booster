package com.tschanz.geobooster.rtm_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.util.model.Tuple2;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;


@RequiredArgsConstructor
public class SqlRgKorridorTkIdsMapping implements SqlStandardMapping<Tuple2<Long, Long>, SqlLongFilter, Long> {
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
    public Tuple2<Long, Long> mapRow(ResultSet row, int rowNum) {
        return new Tuple2<>(
            row.getLong(COL_ID_RELATIONSKORRIDOR_V),
            row.getLong(COL_ID_TARIFKANTE_E)
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
