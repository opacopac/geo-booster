package com.tschanz.geobooster.rtm_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardConverter;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabeVersion;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;


public class SqlHaltestelleWegangabeVersionConverter implements SqlStandardConverter<HaltestelleWegangabeVersion, SqlLongFilter, Long> {
    @Override
    public String getTable() {
        return "R_HALTESTELLEN_WEGANGABE_V";
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
    public HaltestelleWegangabeVersion fromResultSet(ResultSet row) {
        return new HaltestelleWegangabeVersion(
            SqlHasIdConverter.getId(row),
            SqlVersionConverter.getElementId(row),
            SqlVersionConverter.getGueltigVon(row),
            SqlVersionConverter.getGueltigBis(row)
        );
    }


    @Override
    public HaltestelleWegangabeVersion fromJsonAgg(JsonReader reader) {
        return new HaltestelleWegangabeVersion(
            SqlHasIdConverter.getIdFromJsonAgg(reader),
            SqlVersionConverter.getElementIdFromJsonAgg(reader),
            SqlVersionConverter.getGueltigVonFromJsonAgg(reader),
            SqlVersionConverter.getGueltigBisFromJsonAgg(reader)
        );
    }
}
