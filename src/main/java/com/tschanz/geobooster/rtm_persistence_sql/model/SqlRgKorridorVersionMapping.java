package com.tschanz.geobooster.rtm_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.rtm.model.RgKorridorVersion;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdMapping;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionMapping;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;


@RequiredArgsConstructor
public class SqlRgKorridorVersionMapping implements SqlStandardMapping<RgKorridorVersion, SqlLongFilter, Long> {
    public static final String TABLE_NAME = "R_RELATIONSKORRIDOR_V";

    private final Map<Long, Collection<Long>> korrTkMap;
    private final Collection<Long> filterVersionIds;


    @Override
    public String getTable() {
        return TABLE_NAME;
    }


    @Override
    public String[] getSelectFields() {
        return SqlVersionMapping.SELECT_COLS;
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return SqlLongFilter.createSingleton(SqlHasIdMapping.COL_ID, this.filterVersionIds);
    }


    @SneakyThrows
    @Override
    public RgKorridorVersion fromResultSet(ResultSet row) {
        return this.createRgKorridorVersion(
            SqlHasIdMapping.getId(row),
            SqlVersionMapping.getElementId(row),
            SqlVersionMapping.getGueltigVon(row),
            SqlVersionMapping.getGueltigBis(row)
        );
    }


    @Override
    public RgKorridorVersion fromJsonAgg(JsonReader reader) {
        return this.createRgKorridorVersion(
            SqlHasIdMapping.getIdFromJsonAgg(reader),
            SqlVersionMapping.getElementIdFromJsonAgg(reader),
            SqlVersionMapping.getGueltigVonFromJsonAgg(reader),
            SqlVersionMapping.getGueltigBisFromJsonAgg(reader)
        );
    }


    private RgKorridorVersion createRgKorridorVersion(
        long id,
        long elementId,
        LocalDate gueltigVon,
        LocalDate gueltigBis
    ) {
        return new RgKorridorVersion(
            id,
            elementId,
            gueltigVon,
            gueltigBis,
            this.korrTkMap.get(id)
        );
    }
}
