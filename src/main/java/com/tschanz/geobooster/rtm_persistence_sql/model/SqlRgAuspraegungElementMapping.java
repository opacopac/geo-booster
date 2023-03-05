package com.tschanz.geobooster.rtm_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.rtm.model.RgAuspraegung;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdMapping;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;


@RequiredArgsConstructor
public class SqlRgAuspraegungElementMapping implements SqlStandardMapping<RgAuspraegung, SqlLongFilter, Long> {
    private final static String COL_ID_RELATIONSGEBIET_E = "ID_RELATIONSGEBIET_E";

    private final Collection<Long> filterElementIds;


    @Override
    public String getTable() {
        return "R_RG_AUSPRAEGUNG_E";
    }


    @Override
    public String[] getSelectFields() {
        return new String[] { SqlHasIdMapping.COL_ID, COL_ID_RELATIONSGEBIET_E };
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return SqlLongFilter.createSingleton(SqlHasIdMapping.COL_ID, this.filterElementIds);
    }


    @Override
    @SneakyThrows
    public RgAuspraegung mapRow(ResultSet row, int rowNum) {
        return new RgAuspraegung(
            SqlHasIdMapping.getId(row),
            row.getLong(COL_ID_RELATIONSGEBIET_E)
        );
    }


    @Override
    @SneakyThrows
    public RgAuspraegung fromJsonAgg(JsonReader reader) {
        return new RgAuspraegung(
            SqlHasIdMapping.getIdFromJsonAgg(reader),
            reader.nextLong()
        );
    }
}
