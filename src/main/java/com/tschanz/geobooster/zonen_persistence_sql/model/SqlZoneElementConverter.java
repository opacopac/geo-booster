package com.tschanz.geobooster.zonen_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardConverter;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlElementConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.zonen.model.Zone;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;


@RequiredArgsConstructor
public class SqlZoneElementConverter implements SqlStandardConverter<Zone, SqlLongFilter, Long> {
    private final static String COL_ID_ZONENPLAN = "ID_ZONENPLAN";


    @Override
    public String getTable() {
        return "Z_ZONE_E";
    }


    @Override
    public String[] getSelectFields() {
        return ArrayHelper.appendTo(SqlElementConverter.SELECT_COLS, COL_ID_ZONENPLAN);
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return Collections.emptyList();
    }


    @Override
    @SneakyThrows
    public Zone fromResultSet(ResultSet row) {
        return new Zone(
            SqlHasIdConverter.getId(row),
            row.getLong(COL_ID_ZONENPLAN)
        );
    }


    @Override
    @SneakyThrows
    public Zone fromJsonAgg(JsonReader reader) {
        return new Zone(
            SqlHasIdConverter.getIdFromJsonAgg(reader),
            reader.nextLong()
        );
    }
}
