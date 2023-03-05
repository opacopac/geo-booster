package com.tschanz.geobooster.netz_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.netz.model.Verkehrskante;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdMapping;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;


public class SqlVerkehrskanteElementMapping implements SqlStandardMapping<Verkehrskante, SqlLongFilter, Long> {
    private final static String COL_HST1 = "ID_HS_ELEMENT_1";
    private final static String COL_HST2 = "ID_HS_ELEMENT_2";


    @Override
    public String getTable() {
        return "N_VERKEHRSKANTE_E";
    }


    @Override
    public String[] getSelectFields() {
        return new String[] {
            SqlHasIdMapping.COL_ID,
            COL_HST1,
            COL_HST2
        };
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return Collections.emptyList();
    }


    @Override
    @SneakyThrows
    public Verkehrskante mapRow(ResultSet row, int rowNum) {
        return new Verkehrskante(
            SqlHasIdMapping.getId(row),
            row.getLong(COL_HST1),
            row.getLong(COL_HST2)
        );
    }


    @Override
    @SneakyThrows
    public Verkehrskante fromJsonAgg(JsonReader reader) {
        return new Verkehrskante(
            SqlHasIdMapping.getIdFromJsonAgg(reader),
            reader.nextLong(),
            reader.nextLong()
        );
    }
}
