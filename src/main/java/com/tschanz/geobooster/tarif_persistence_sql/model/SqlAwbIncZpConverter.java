package com.tschanz.geobooster.tarif_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardConverter;
import com.tschanz.geobooster.util.model.KeyValue;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;


@RequiredArgsConstructor
public class SqlAwbIncZpConverter implements SqlStandardConverter<KeyValue<Long, Long>, SqlLongFilter, Long> {
    private final static String COL_ID_ANWBER_V = "ID_ANWBER_V";
    private final static String COL_ID_ZONENPLAN_E = "ID_ZONENPLAN_E";

    private final Collection<Long> awbVersionIds;


    @Override
    public String getTable() {
        return "T_ANWBER_X_INCLUDE_ZONENPLAN";
    }


    @Override
    public String[] getSelectFields() {
        return new String[] { COL_ID_ANWBER_V, COL_ID_ZONENPLAN_E };
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return SqlLongFilter.createSingleton(COL_ID_ANWBER_V, this.awbVersionIds);
    }


    @Override
    @SneakyThrows
    public KeyValue<Long, Long> fromResultSet(ResultSet row) {
        return new KeyValue<>(
            row.getLong(COL_ID_ANWBER_V),
            row.getLong(COL_ID_ZONENPLAN_E)
        );
    }


    @Override
    public KeyValue<Long, Long> fromJsonAgg(JsonReader reader) {
        return null;
    }
}
