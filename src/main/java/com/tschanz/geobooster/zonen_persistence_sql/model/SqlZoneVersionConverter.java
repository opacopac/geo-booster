package com.tschanz.geobooster.zonen_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardConverter;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import com.tschanz.geobooster.zonen.model.ZoneVersion;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;


public class SqlZoneVersionConverter implements SqlStandardConverter<ZoneVersion, SqlLongFilter, Long> {
    private final static String COL_ID_URSPRUNGSZONE_E = "ID_URSPRUNGSZONE_E";


    @Override
    public String getTable() {
        return "Z_ZONE_V";
    }


    @Override
    public String[] getSelectFields() {
        return ArrayHelper.appendTo(SqlVersionConverter.SELECT_COLS, COL_ID_URSPRUNGSZONE_E);
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return Collections.emptyList();
    }


    @SneakyThrows
    @Override
    public ZoneVersion fromResultSet(ResultSet row) {
        return new ZoneVersion(
            SqlHasIdConverter.getId(row),
            SqlVersionConverter.getElementId(row),
            SqlVersionConverter.getGueltigVon(row),
            SqlVersionConverter.getGueltigBis(row),
            row.getLong(COL_ID_URSPRUNGSZONE_E)
        );
    }


    @Override
    @SneakyThrows
    public ZoneVersion fromJsonAgg(JsonReader reader) {
        return new ZoneVersion(
            SqlHasIdConverter.getIdFromJsonAgg(reader),
            SqlVersionConverter.getElementIdFromJsonAgg(reader),
            SqlVersionConverter.getGueltigVonFromJsonAgg(reader),
            SqlVersionConverter.getGueltigBisFromJsonAgg(reader),
            reader.nextLong()
        );
    }
}
