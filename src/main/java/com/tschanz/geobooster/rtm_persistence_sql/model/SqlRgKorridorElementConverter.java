package com.tschanz.geobooster.rtm_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardConverter;
import com.tschanz.geobooster.rtm.model.RgKorridor;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;


@RequiredArgsConstructor
public class SqlRgKorridorElementConverter implements SqlStandardConverter<RgKorridor, SqlLongFilter, Long> {
    private final static String COL_ID_RELATIONSGEBIET_E = "ID_RELATIONSGEBIET_E";


    @Override
    public String getTable() {
        return "R_RELATIONSKORRIDOR_E";
    }


    @Override
    public String[] getSelectFields() {
        return new String[] { SqlHasIdConverter.COL_ID, COL_ID_RELATIONSGEBIET_E };
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return Collections.emptyList();
    }


    @Override
    @SneakyThrows
    public RgKorridor fromResultSet(ResultSet row) {
        return new RgKorridor(
            SqlHasIdConverter.getId(row),
            row.getLong(COL_ID_RELATIONSGEBIET_E)
        );
    }


    @Override
    @SneakyThrows
    public RgKorridor fromJsonAgg(JsonReader reader) {
        return new RgKorridor(
            SqlHasIdConverter.getIdFromJsonAgg(reader),
            reader.nextLong()
        );
    }
}
