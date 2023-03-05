package com.tschanz.geobooster.netz_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.netz.model.VerwaltungVersion;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.versioning.model.Pflegestatus;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdMapping;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionMapping;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;


public class SqlVerwaltungVersionMapping implements SqlStandardMapping<VerwaltungVersion, SqlLongFilter, Long> {
    @Override
    public String getTable() {
        return "N_VERWALTUNG_V";
    }


    @Override
    public String[] getSelectFields() {
        return SqlVersionMapping.SELECT_COLS;
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return Collections.emptyList();
    }


    @SneakyThrows
    public VerwaltungVersion mapRow(ResultSet row, int rowNum) {
        return new VerwaltungVersion(
            SqlHasIdMapping.getId(row),
            SqlVersionMapping.getElementId(row),
            SqlVersionMapping.getGueltigVon(row),
            SqlVersionMapping.getGueltigBis(row),
            Pflegestatus.PRODUKTIV
        );
    }


    @Override
    public VerwaltungVersion fromJsonAgg(JsonReader reader) {
        return new VerwaltungVersion(
            SqlHasIdMapping.getIdFromJsonAgg(reader),
            SqlVersionMapping.getElementIdFromJsonAgg(reader),
            SqlVersionMapping.getGueltigVonFromJsonAgg(reader),
            SqlVersionMapping.getGueltigBisFromJsonAgg(reader),
            Pflegestatus.PRODUKTIV
        );
    }
}
