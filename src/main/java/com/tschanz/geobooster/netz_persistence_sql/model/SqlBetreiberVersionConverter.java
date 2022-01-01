package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.BetreiberVersion;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlBetreiberVersionConverter implements SqlResultsetConverter<BetreiberVersion> {
    public final static String[] SELECT_COLS = SqlVersionConverter.SELECT_COLS;


    @Override
    @SneakyThrows
    public BetreiberVersion fromResultSet(ResultSet row) {
        return new BetreiberVersion(
            SqlVersionConverter.getId(row),
            SqlVersionConverter.getElementId(row),
            SqlVersionConverter.getGueltigVon(row),
            SqlVersionConverter.getGueltigBis(row)
        );
    }
}
