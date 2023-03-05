package com.tschanz.geobooster.zonen_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.versioning.model.Pflegestatus;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdMapping;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionMapping;
import com.tschanz.geobooster.zonen.model.ZonenplanVersion;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;


@RequiredArgsConstructor
public class SqlZonenplanVersionMapping implements SqlStandardMapping<ZonenplanVersion, SqlLongFilter, Long> {
    public static String TABLE_NAME = "Z_ZONENPLAN_V";

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
    public ZonenplanVersion mapRow(ResultSet row, int rowNum) {
        return new ZonenplanVersion(
            SqlHasIdMapping.getId(row),
            SqlVersionMapping.getElementId(row),
            SqlVersionMapping.getGueltigVon(row),
            SqlVersionMapping.getGueltigBis(row),
            Pflegestatus.PRODUKTIV
        );
    }


    @Override
    public ZonenplanVersion fromJsonAgg(JsonReader reader) {
        return new ZonenplanVersion(
            SqlHasIdMapping.getIdFromJsonAgg(reader),
            SqlVersionMapping.getElementIdFromJsonAgg(reader),
            SqlVersionMapping.getGueltigVonFromJsonAgg(reader),
            SqlVersionMapping.getGueltigBisFromJsonAgg(reader),
            Pflegestatus.PRODUKTIV
        );
    }
}
