package com.tschanz.geobooster.tarif_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.util.model.Tuple2;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;


@RequiredArgsConstructor
public class SqlAwbIncVkaMapping implements SqlStandardMapping<Tuple2<Long, Long>, SqlLongFilter, Long> {
    private final static String COL_ID_ANWBER_V = "ID_ANWBER_V";
    private final static String COL_ID_KANTEN_AUSPRAEGUNG_E = "ID_KANTEN_AUSPRAEGUNG_E";

    private final Collection<Long> awbVersionIds;


    @Override
    public String getTable() {
        return "T_ANWBER_X_INCLUDE_KANTEN";
    }


    @Override
    public String[] getSelectFields() {
        return new String[] { COL_ID_ANWBER_V, COL_ID_KANTEN_AUSPRAEGUNG_E };
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return SqlLongFilter.createSingleton(COL_ID_ANWBER_V, this.awbVersionIds);
    }


    @Override
    @SneakyThrows
    public Tuple2<Long, Long> fromResultSet(ResultSet row) {
        return new Tuple2<>(
            row.getLong(COL_ID_ANWBER_V),
            row.getLong(COL_ID_KANTEN_AUSPRAEGUNG_E)
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
