package com.tschanz.geobooster.rtm_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardConverter;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabe;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlElementConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;


@RequiredArgsConstructor
public class SqlHaltestelleWegangabeElementConverter implements SqlStandardConverter<HaltestelleWegangabe, SqlLongFilter, Long> {
    private final static String COL_CODE = "CODE";


    @Override
    public String getTable() {
        return "R_HALTESTELLEN_WEGANGABE_E";
    }


    @Override
    public String[] getSelectFields() {
        return ArrayHelper.appendTo(SqlElementConverter.SELECT_COLS, COL_CODE);
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return Collections.emptyList();
    }


    @Override
    @SneakyThrows
    public HaltestelleWegangabe fromResultSet(ResultSet row) {
        return new HaltestelleWegangabe(
            SqlHasIdConverter.getId(row),
            row.getInt(COL_CODE)
        );
    }


    @Override
    @SneakyThrows
    public HaltestelleWegangabe fromJsonAgg(JsonReader reader) {
        return new HaltestelleWegangabe(
            SqlHasIdConverter.getIdFromJsonAgg(reader),
            reader.nextInt()
        );
    }
}
