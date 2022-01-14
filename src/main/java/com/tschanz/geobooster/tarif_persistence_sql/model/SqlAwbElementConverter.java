package com.tschanz.geobooster.tarif_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardConverter;
import com.tschanz.geobooster.tarif.model.Awb;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;


@RequiredArgsConstructor
public class SqlAwbElementConverter implements SqlStandardConverter<Awb, SqlLongFilter, Long> {
    private final Collection<Long> elementIds;


    public String getTable() {
        return "T_ANWBER_E";
    }


    @Override
    public String[] getSelectFields() {
        return new String[] { SqlHasIdConverter.COL_ID };
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return SqlLongFilter.createSingleton(SqlHasIdConverter.COL_ID, this.elementIds);
    }


    @Override
    @SneakyThrows
    public Awb fromResultSet(ResultSet row) {
        return new Awb(
            SqlHasIdConverter.getId(row)
        );
    }


    @Override
    public Awb fromJsonAgg(JsonReader reader) {
        return new Awb(
            SqlHasIdConverter.getIdFromJsonAgg(reader)
        );
    }
}
