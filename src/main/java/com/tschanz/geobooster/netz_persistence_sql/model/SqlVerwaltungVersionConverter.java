package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.VerwaltungVersion;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlVerwaltungVersionConverter implements SqlResultsetConverter<VerwaltungVersion> {
    public final static String[] SELECT_COLS = SqlVersionConverter.SELECT_COLS;


    @SneakyThrows
    public VerwaltungVersion fromResultSet(ResultSet row) {
        return new VerwaltungVersion(
            SqlHasIdConverter.getId(row),
            SqlVersionConverter.getElementId(row),
            SqlVersionConverter.getGueltigVon(row),
            SqlVersionConverter.getGueltigBis(row)
        );
    }
}
