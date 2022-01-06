package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlVerkehrskanteCountConverter implements SqlResultsetConverter<Long> {
    private final static String COL_COUNT = "TKV_COUNT";


    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT COUNT(*) AS %s FROM N_TARIFKANTE_V",
            COL_COUNT
        );
    }


    @Override
    @SneakyThrows
    public Long fromResultSet(ResultSet row) {
        return row.getLong(COL_COUNT);
    }
}
