package com.tschanz.geobooster.netz_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdMapping;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;


public class SqlHaltestelleElementMapping implements SqlStandardMapping<Haltestelle, SqlLongFilter, Long> {
    private final static String COL_UIC = "UIC_CODE";


    @Override
    public String getTable() {
        return "N_HALTESTELLE_E";
    }


    @Override
    public String[] getSelectFields() {
        return new String[] {
            SqlHasIdMapping.COL_ID,
            COL_UIC
        };
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return Collections.emptyList();
    }


    @Override
    @SneakyThrows
    public Haltestelle mapRow(ResultSet row, int rowNum) {
        return new Haltestelle(
            SqlHasIdMapping.getId(row),
            row.getInt(COL_UIC)
        );
    }


    @Override
    @SneakyThrows
    public Haltestelle fromJsonAgg(JsonReader reader) {
        return new Haltestelle(
            SqlHasIdMapping.getIdFromJsonAgg(reader),
            reader.nextInt()
        );
    }
}
