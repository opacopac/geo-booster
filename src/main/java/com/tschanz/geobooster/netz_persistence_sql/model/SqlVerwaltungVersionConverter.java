package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.VerwaltungVersion;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlVerwaltungVersionConverter implements SqlResultsetConverter<VerwaltungVersion> {
    public final static String[] ALL_COLS = SqlVersionConverter.ALL_COLS;


    @SneakyThrows
    public VerwaltungVersion fromResultSet(ResultSet row) {
        return new VerwaltungVersion(
            SqlVersionConverter.getId(row),
            SqlVersionConverter.getElementId(row),
            SqlVersionConverter.getGueltigVon(row),
            SqlVersionConverter.getGueltigBis(row)
        );
    }
}
