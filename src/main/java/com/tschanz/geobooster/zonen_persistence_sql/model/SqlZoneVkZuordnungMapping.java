package com.tschanz.geobooster.zonen_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdMapping;
import com.tschanz.geobooster.zonen.model.ZoneVkZuordnung;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;


@RequiredArgsConstructor
public class SqlZoneVkZuordnungMapping implements SqlStandardMapping<ZoneVkZuordnung, SqlLongFilter, Long> {
    public final static String TABLE_NAME = "Z_ZONE_KANTE_ZUORDNUNG";
    private final static String COL_ID_ZONE_VERSION = "ID_ZONE_VERSION";
    private final static String COL_ID_KANTE_ELEMENT = "ID_KANTE_ELEMENT";

    private final Collection<Long> filterVersionIds;

    @Override
    public String getTable() {
        return TABLE_NAME;
    }


    @Override
    public String[] getSelectFields() {
        return new String[] { SqlHasIdMapping.COL_ID, COL_ID_ZONE_VERSION, COL_ID_KANTE_ELEMENT };
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return SqlLongFilter.createSingleton(SqlHasIdMapping.COL_ID, this.filterVersionIds);
    }


    @SneakyThrows
    public ZoneVkZuordnung mapRow(ResultSet row, int rowNum) {
        return new ZoneVkZuordnung(
            SqlHasIdMapping.getId(row),
            row.getLong(COL_ID_ZONE_VERSION),
            row.getLong(COL_ID_KANTE_ELEMENT)
        );
    }


    @SneakyThrows
    public ZoneVkZuordnung fromJsonAgg(JsonReader reader) {
        return new ZoneVkZuordnung(
            SqlHasIdMapping.getIdFromJsonAgg(reader),
            reader.nextLong(),
            reader.nextLong()
        );
    }
}
