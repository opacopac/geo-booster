package com.tschanz.geobooster.rtm_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.rtm.model.RgKorridor;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdMapping;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;


@RequiredArgsConstructor
public class SqlRgKorridorElementMapping implements SqlStandardMapping<RgKorridor, SqlLongFilter, Long> {
    private final static String COL_ID_RELATIONSGEBIET_E = "ID_RELATIONSGEBIET_E";

    private final Collection<Long> filterElementIds;


    @Override
    public String getTable() {
        return "R_RELATIONSKORRIDOR_E";
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
    public RgKorridor mapRow(ResultSet row, int rowNum) {
        return new RgKorridor(
            SqlHasIdMapping.getId(row),
            row.getLong(COL_ID_RELATIONSGEBIET_E)
        );
    }


    @Override
    @SneakyThrows
    public RgKorridor fromJsonAgg(JsonReader reader) {
        return new RgKorridor(
            SqlHasIdMapping.getIdFromJsonAgg(reader),
            reader.nextLong()
        );
    }
}
