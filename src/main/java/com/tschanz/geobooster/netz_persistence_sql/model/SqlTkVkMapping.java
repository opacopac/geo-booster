package com.tschanz.geobooster.netz_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.util.model.KeyValue;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;


@RequiredArgsConstructor
public class SqlTkVkMapping implements SqlStandardMapping<KeyValue<Long, Long>, SqlLongFilter, Long> {
    private final static String COL_ID_TARIFKANTE_V = "ID_TARIFKANTE_V";
    private final static String COL_ID_VERKEHRS_KANTE_E = "ID_VERKEHRS_KANTE_E";

    private final List<Long> filterTkVIds;


    @Override
    public String getTable() {
        return "N_TARIFKANTE_X_N_VERK_KANTE_E";
    }


    @Override
    public String[] getSelectFields() {
        return new String[] { COL_ID_TARIFKANTE_V, COL_ID_VERKEHRS_KANTE_E };
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return SqlLongFilter.createSingleton(COL_ID_TARIFKANTE_V, this.filterTkVIds);
    }


    @Override
    @SneakyThrows
    public KeyValue<Long, Long> fromResultSet(ResultSet row) {
        return new KeyValue<>(
            row.getLong(COL_ID_TARIFKANTE_V),
            row.getLong(COL_ID_VERKEHRS_KANTE_E)
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
