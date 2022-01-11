package com.tschanz.geobooster.netz_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.persistence_sql.model.SqlJsonAggConverter;
import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlHaltestelleElementConverter implements SqlResultsetConverter<Haltestelle>, SqlJsonAggConverter<Haltestelle> {
    private final static String COL_UIC = "UIC_CODE";


    @Override
    public String getTable() {
        return "N_HALTESTELLE_E";
    }


    @Override
    public String[] getFields() {
        return new String[] {
            SqlHasIdConverter.COL_ID,
            COL_UIC
        };
    }


    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM %s",
            String.join(",", this.getFields()),
            this.getTable()
        );
    }


    @Override
    @SneakyThrows
    public Haltestelle fromResultSet(ResultSet row) {
        return new Haltestelle(
            SqlHasIdConverter.getId(row),
            row.getInt(COL_UIC)
        );
    }


    @Override
    @SneakyThrows
    public Haltestelle fromJsonAgg(JsonReader reader) {
        return new Haltestelle(
            SqlHasIdConverter.getIdFromJsonAgg(reader),
            reader.nextInt()
        );
    }
}
