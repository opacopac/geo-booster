package com.tschanz.geobooster.zonen_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlElementMapping;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdMapping;
import com.tschanz.geobooster.zonen.model.Zone;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;


@RequiredArgsConstructor
public class SqlZoneElementMapping implements SqlStandardMapping<Zone, SqlLongFilter, Long> {
    private final static String COL_ID_ZONENPLAN = "ID_ZONENPLAN";

    private final Collection<Long> elementIds;


    @Override
    public String getTable() {
        return "Z_ZONE_E";
    }


    @Override
    public String[] getSelectFields() {
        return ArrayHelper.appendTo(SqlElementMapping.SELECT_COLS, COL_ID_ZONENPLAN);
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return SqlLongFilter.createSingleton(SqlHasIdMapping.COL_ID, this.elementIds);
    }


    @Override
    @SneakyThrows
    public Zone fromResultSet(ResultSet row) {
        return new Zone(
            SqlHasIdMapping.getId(row),
            row.getLong(COL_ID_ZONENPLAN)
        );
    }


    @Override
    @SneakyThrows
    public Zone fromJsonAgg(JsonReader reader) {
        return new Zone(
            SqlHasIdMapping.getIdFromJsonAgg(reader),
            reader.nextLong()
        );
    }
}
