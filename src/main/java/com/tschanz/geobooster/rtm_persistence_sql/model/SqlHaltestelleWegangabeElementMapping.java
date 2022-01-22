package com.tschanz.geobooster.rtm_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabe;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlElementMapping;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdMapping;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;


@RequiredArgsConstructor
public class SqlHaltestelleWegangabeElementMapping implements SqlStandardMapping<HaltestelleWegangabe, SqlLongFilter, Long> {
    private final static String COL_CODE = "CODE";

    private final Collection<Long> filterElementIds;


    @Override
    public String getTable() {
        return "R_HALTESTELLEN_WEGANGABE_E";
    }


    @Override
    public String[] getSelectFields() {
        return ArrayHelper.appendTo(SqlElementMapping.SELECT_COLS, COL_CODE);
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return SqlLongFilter.createSingleton(SqlHasIdMapping.COL_ID, this.filterElementIds);
    }


    @Override
    @SneakyThrows
    public HaltestelleWegangabe fromResultSet(ResultSet row) {
        return new HaltestelleWegangabe(
            SqlHasIdMapping.getId(row),
            row.getInt(COL_CODE)
        );
    }


    @Override
    @SneakyThrows
    public HaltestelleWegangabe fromJsonAgg(JsonReader reader) {
        return new HaltestelleWegangabe(
            SqlHasIdMapping.getIdFromJsonAgg(reader),
            reader.nextInt()
        );
    }
}
