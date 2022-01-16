package com.tschanz.geobooster.rtm_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabeVersion;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdMapping;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionMapping;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;


public class SqlHaltestelleWegangabeVersionMapping implements SqlStandardMapping<HaltestelleWegangabeVersion, SqlLongFilter, Long> {
    @Override
    public String getTable() {
        return "R_HALTESTELLEN_WEGANGABE_V";
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
    @Override
    public HaltestelleWegangabeVersion fromResultSet(ResultSet row) {
        return new HaltestelleWegangabeVersion(
            SqlHasIdMapping.getId(row),
            SqlVersionMapping.getElementId(row),
            SqlVersionMapping.getGueltigVon(row),
            SqlVersionMapping.getGueltigBis(row)
        );
    }


    @Override
    public HaltestelleWegangabeVersion fromJsonAgg(JsonReader reader) {
        return new HaltestelleWegangabeVersion(
            SqlHasIdMapping.getIdFromJsonAgg(reader),
            SqlVersionMapping.getElementIdFromJsonAgg(reader),
            SqlVersionMapping.getGueltigVonFromJsonAgg(reader),
            SqlVersionMapping.getGueltigBisFromJsonAgg(reader)
        );
    }
}
