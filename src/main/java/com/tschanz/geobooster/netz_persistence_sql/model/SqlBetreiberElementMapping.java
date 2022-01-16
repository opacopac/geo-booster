package com.tschanz.geobooster.netz_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdMapping;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;


public class SqlBetreiberElementMapping implements SqlStandardMapping<Betreiber, SqlLongFilter, Long> {
    private final static String COL_NAME = "NAME";
    private final static String COL_ABK = "ABKUERZUNG";


    @Override
    public String getTable() {
        return "N_BETREIBER_E";
    }


    @Override
    public String[] getSelectFields() {
        return new String[] {
            SqlHasIdMapping.COL_ID,
            COL_NAME,
            COL_ABK
        };
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return Collections.emptyList();
    }


    @Override
    @SneakyThrows
    public Betreiber fromResultSet(ResultSet row) {
        return new Betreiber(
            SqlHasIdMapping.getId(row),
            row.getString(COL_NAME),
            row.getString(COL_ABK)
        );
    }


    @Override
    @SneakyThrows
    public Betreiber fromJsonAgg(JsonReader reader) {
        return new Betreiber(
            SqlHasIdMapping.getIdFromJsonAgg(reader),
            reader.nextString(),
            reader.nextString()
        );
    }
}
