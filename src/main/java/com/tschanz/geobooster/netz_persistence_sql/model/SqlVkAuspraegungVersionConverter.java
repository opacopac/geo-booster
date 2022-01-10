package com.tschanz.geobooster.netz_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegungVersion;
import com.tschanz.geobooster.persistence_sql.model.SqlJsonAggConverter;
import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlVkAuspraegungVersionConverter implements SqlResultsetConverter<VerkehrskanteAuspraegungVersion>, SqlJsonAggConverter<VerkehrskanteAuspraegungVersion> {
    @Override
    public String getTable() {
        return "N_VERKEHRS_KANTE_AUSPR_V";
    }


    @Override
    public String[] getFields() {
        return SqlVersionConverter.SELECT_COLS;
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
    public VerkehrskanteAuspraegungVersion fromResultSet(ResultSet row) {
        return new VerkehrskanteAuspraegungVersion(
            SqlHasIdConverter.getId(row),
            SqlVersionConverter.getElementId(row),
            SqlVersionConverter.getGueltigVon(row),
            SqlVersionConverter.getGueltigBis(row)
        );
    }


    @Override
    @SneakyThrows
    public VerkehrskanteAuspraegungVersion fromJsonAgg(JsonReader reader) {
        return new VerkehrskanteAuspraegungVersion(
            SqlHasIdConverter.getIdFromJsonAgg(reader),
            SqlVersionConverter.getElementIdFromJsonAgg(reader),
            SqlVersionConverter.getGueltigVonFromJsonAgg(reader),
            SqlVersionConverter.getGueltigBisFromJsonAgg(reader)
        );
    }
}
