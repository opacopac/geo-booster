package com.tschanz.geobooster.netz_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.netz.model.VerwaltungVersion;
import com.tschanz.geobooster.persistence_sql.model.SqlJsonAggConverter;
import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlVerwaltungVersionConverter implements SqlResultsetConverter<VerwaltungVersion>, SqlJsonAggConverter<VerwaltungVersion> {
    @Override
    public String getTable() {
        return "N_VERWALTUNG_V";
    }


    @Override
    public String[] getSelectFields() {
        return SqlVersionConverter.SELECT_COLS;
    }



    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM %s",
            String.join(",", this.getSelectFields()),
            this.getTable()
        );
    }


    @SneakyThrows
    public VerwaltungVersion fromResultSet(ResultSet row) {
        return new VerwaltungVersion(
            SqlHasIdConverter.getId(row),
            SqlVersionConverter.getElementId(row),
            SqlVersionConverter.getGueltigVon(row),
            SqlVersionConverter.getGueltigBis(row)
        );
    }


    @Override
    public VerwaltungVersion fromJsonAgg(JsonReader reader) {
        return new VerwaltungVersion(
            SqlHasIdConverter.getIdFromJsonAgg(reader),
            SqlVersionConverter.getElementIdFromJsonAgg(reader),
            SqlVersionConverter.getGueltigVonFromJsonAgg(reader),
            SqlVersionConverter.getGueltigBisFromJsonAgg(reader)
        );
    }
}
