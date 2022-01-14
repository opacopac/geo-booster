package com.tschanz.geobooster.rtm_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardConverter;
import com.tschanz.geobooster.rtm.model.RgKorridorVersion;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;


public class SqlRgKorridorVersionConverter implements SqlStandardConverter<RgKorridorVersion, SqlLongFilter, Long> {
    @Override
    public String getTable() {
        return "R_RELATIONSKORRIDOR_V";
    }


    @Override
    public String[] getSelectFields() {
        return SqlVersionConverter.SELECT_COLS;
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return Collections.emptyList();
    }


    @SneakyThrows
    @Override
    public RgKorridorVersion fromResultSet(ResultSet row) {
        return new RgKorridorVersion(
            SqlHasIdConverter.getId(row),
            SqlVersionConverter.getElementId(row),
            SqlVersionConverter.getGueltigVon(row),
            SqlVersionConverter.getGueltigBis(row)
        );
    }


    @Override
    public RgKorridorVersion fromJsonAgg(JsonReader reader) {
        return new RgKorridorVersion(
            SqlHasIdConverter.getIdFromJsonAgg(reader),
            SqlVersionConverter.getElementIdFromJsonAgg(reader),
            SqlVersionConverter.getGueltigVonFromJsonAgg(reader),
            SqlVersionConverter.getGueltigBisFromJsonAgg(reader)
        );
    }
}
