package com.tschanz.geobooster.netz_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.persistence_sql.model.SqlJsonAggConverter;
import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlBetreiberElementConverter implements SqlResultsetConverter<Betreiber>, SqlJsonAggConverter<Betreiber> {
    private final static String COL_NAME = "NAME";
    private final static String COL_ABK = "ABKUERZUNG";


    @Override
    public String getTable() {
        return "N_BETREIBER_E";
    }


    @Override
    public String[] getFields() {
        return new String[] {
            SqlHasIdConverter.COL_ID,
            COL_NAME,
            COL_ABK
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
    public Betreiber fromResultSet(ResultSet row) {
        return new Betreiber(
            SqlHasIdConverter.getId(row),
            row.getString(COL_NAME),
            row.getString(COL_ABK)
        );
    }


    @Override
    @SneakyThrows
    public Betreiber fromJsonAgg(JsonReader reader) {
        return new Betreiber(
            SqlHasIdConverter.getIdFromJsonAgg(reader),
            reader.nextString(),
            reader.nextString()
        );
    }
}
