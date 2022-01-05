package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegungVersion;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlVerkehrskanteAuspraegungVersionConverter implements SqlResultsetConverter<VerkehrskanteAuspraegungVersion> {
    public final static String[] SELECT_COLS = SqlVersionConverter.SELECT_COLS;


    @SneakyThrows
    @Override
    public VerkehrskanteAuspraegungVersion fromResultSet(ResultSet row) {
        return new VerkehrskanteAuspraegungVersion(
            SqlHasIdConverter.getId(row),
            SqlVersionConverter.getElementId(row),
            SqlVersionConverter.getGueltigVon(row),
            SqlVersionConverter.getGueltigBis(row)
        );
    }
}
