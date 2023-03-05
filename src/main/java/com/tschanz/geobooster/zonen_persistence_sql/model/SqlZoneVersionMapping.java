package com.tschanz.geobooster.zonen_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.persistence_sql.service.SqlHelper;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning.model.Pflegestatus;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdMapping;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionMapping;
import com.tschanz.geobooster.zonen.model.ZoneVersion;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Collection;


@RequiredArgsConstructor
public class SqlZoneVersionMapping implements SqlStandardMapping<ZoneVersion, SqlLongFilter, Long> {
    public final static String TABLE_NAME = "Z_ZONE_V";
    private final static String COL_ID_URSPRUNGSZONE_E = "ID_URSPRUNGSZONE_E";

    private final Collection<Long> filterVersionIds;


    @Override
    public String getTable() {
        return TABLE_NAME;
    }


    @Override
    public String[] getSelectFields() {
        return ArrayHelper.appendTo(SqlVersionMapping.SELECT_COLS, COL_ID_URSPRUNGSZONE_E);
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return SqlLongFilter.createSingleton(SqlHasIdMapping.COL_ID, this.filterVersionIds);
    }


    @SneakyThrows
    @Override
    public ZoneVersion mapRow(ResultSet row, int rowNum) {
        return this.createZoneVersion(
            SqlHasIdMapping.getId(row),
            SqlVersionMapping.getElementId(row),
            SqlVersionMapping.getGueltigVon(row),
            SqlVersionMapping.getGueltigBis(row),
            row.getLong(COL_ID_URSPRUNGSZONE_E)
        );
    }


    @Override
    @SneakyThrows
    public ZoneVersion fromJsonAgg(JsonReader reader) {
        return this.createZoneVersion(
            SqlHasIdMapping.getIdFromJsonAgg(reader),
            SqlVersionMapping.getElementIdFromJsonAgg(reader),
            SqlVersionMapping.getGueltigVonFromJsonAgg(reader),
            SqlVersionMapping.getGueltigBisFromJsonAgg(reader),
            SqlHelper.parseLongOrDefaultFromJsonAgg(reader, 0)
        );
    }


    private ZoneVersion createZoneVersion(
        long id,
        long elementId,
        LocalDate gueltigVon,
        LocalDate gueltigBis,
        Long ursprungsZone
    ) {
        return new ZoneVersion(
            id,
            elementId,
            gueltigVon,
            gueltigBis,
            Pflegestatus.PRODUKTIV,
            ursprungsZone
        );
    }
}
