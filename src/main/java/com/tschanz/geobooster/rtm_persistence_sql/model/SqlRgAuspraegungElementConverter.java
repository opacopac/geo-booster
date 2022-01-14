package com.tschanz.geobooster.rtm_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardConverter;
import com.tschanz.geobooster.rtm.model.RgAuspraegung;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;


@RequiredArgsConstructor
public class SqlRgAuspraegungElementConverter implements SqlStandardConverter<RgAuspraegung, SqlLongFilter, Long> {
    private final static String COL_ID_RELATIONSGEBIET_E = "ID_RELATIONSGEBIET_E";


    @Override
    public String getTable() {
        return "R_RG_AUSPRAEGUNG_E";
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
    public RgAuspraegung fromResultSet(ResultSet row) {
        return new RgAuspraegung(
            SqlHasIdConverter.getId(row),
            row.getLong(COL_ID_RELATIONSGEBIET_E)
        );
    }


    @Override
    @SneakyThrows
    public RgAuspraegung fromJsonAgg(JsonReader reader) {
        return new RgAuspraegung(
            SqlHasIdConverter.getIdFromJsonAgg(reader),
            reader.nextLong()
        );
    }
}
