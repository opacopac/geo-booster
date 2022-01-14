package com.tschanz.geobooster.netz_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.netz.model.Verwaltung;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;


public class SqlVerwaltungElementConverter implements SqlStandardConverter<Verwaltung, SqlLongFilter, Long> {
    private final static String COL_CODE = "CODE";
    private final static String COL_IDBETREIBER = "ID_BETREIBER";
    private final static String COL_INFOPLUSTC = "INFO_PLUS_TC";


    @Override
    public String getTable() {
        return "N_VERWALTUNG_E";
    }


    @Override
    public String[] getSelectFields() {
        return new String[] {
            SqlHasIdConverter.COL_ID,
            COL_CODE,
            COL_IDBETREIBER,
            COL_INFOPLUSTC
        };
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return Collections.emptyList();
    }


    @Override
    @SneakyThrows
    public Verwaltung fromResultSet(ResultSet row) {
        return new Verwaltung(
            SqlHasIdConverter.getId(row),
            row.getString(COL_CODE),
            row.getLong(COL_IDBETREIBER),
            row.getString(COL_INFOPLUSTC)
        );
    }


    @Override
    @SneakyThrows
    public Verwaltung fromJsonAgg(JsonReader reader) {
        return new Verwaltung(
            SqlHasIdConverter.getIdFromJsonAgg(reader),
            reader.nextString(),
            reader.nextLong(),
            SqlHelper.parseStringOrNullfromJsonAgg(reader)
        );
    }
}
