package com.tschanz.geobooster.tarif_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.tarif.model.AwbIncVerwaltung;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdMapping;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;


@RequiredArgsConstructor
public class SqlAwbIncVerwaltungMapping implements SqlStandardMapping<AwbIncVerwaltung, SqlLongFilter, Long> {
    public static final String TABLE_NAME = "T_ANWBER_X_INCLUDE_VERWALTUNG";

    private final static String COL_ID_ANWBER_V = "ID_ANWBER_V";
    private final static String ID_VERWALTUNG_E = "ID_VERWALTUNG_E";

    private final Collection<Long> filterIds;


    @Override
    public String getTable() {
        return TABLE_NAME;
    }


    @Override
    public String[] getSelectFields() {
        return new String[] { SqlHasIdMapping.COL_ID, COL_ID_ANWBER_V, ID_VERWALTUNG_E};
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return SqlLongFilter.createSingleton(SqlHasIdMapping.COL_ID, this.filterIds);
    }


    @Override
    @SneakyThrows
    public AwbIncVerwaltung mapRow(ResultSet row, int rowNum) {
        return new AwbIncVerwaltung(
            SqlHasIdMapping.getId(row),
            row.getLong(COL_ID_ANWBER_V),
            row.getLong(ID_VERWALTUNG_E)
        );
    }


    @Override
    @SneakyThrows
    public AwbIncVerwaltung fromJsonAgg(JsonReader reader) {
        return new AwbIncVerwaltung(
            SqlHasIdMapping.getIdFromJsonAgg(reader),
            reader.nextLong(),
            reader.nextLong()
        );
    }
}
