package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlVerkehrskanteVersionIdConverter implements SqlResultsetConverter<Long> {
    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM N_TARIFKANTE_V",
            SqlHasIdConverter.COL_ID
        );
    }


    @SneakyThrows
    @Override
    public Long fromResultSet(ResultSet row) {
        return SqlHasIdConverter.getId(row);
    }
}
