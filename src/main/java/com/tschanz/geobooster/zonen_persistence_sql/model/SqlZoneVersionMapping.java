package com.tschanz.geobooster.zonen_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdMapping;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionMapping;
import com.tschanz.geobooster.zonen.model.ZoneVersion;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;


public class SqlZoneVersionMapping implements SqlStandardMapping<ZoneVersion, SqlLongFilter, Long> {
    private final static String COL_ID_URSPRUNGSZONE_E = "ID_URSPRUNGSZONE_E";


    @Override
    public String getTable() {
        return "Z_ZONE_V";
    }


    @Override
    public String[] getSelectFields() {
        return ArrayHelper.appendTo(SqlVersionMapping.SELECT_COLS, COL_ID_URSPRUNGSZONE_E);
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return Collections.emptyList();
    }


    @SneakyThrows
    @Override
    public ZoneVersion fromResultSet(ResultSet row) {
        return new ZoneVersion(
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
        return new ZoneVersion(
            SqlHasIdMapping.getIdFromJsonAgg(reader),
            SqlVersionMapping.getElementIdFromJsonAgg(reader),
            SqlVersionMapping.getGueltigVonFromJsonAgg(reader),
            SqlVersionMapping.getGueltigBisFromJsonAgg(reader),
            reader.nextLong()
        );
    }
}
