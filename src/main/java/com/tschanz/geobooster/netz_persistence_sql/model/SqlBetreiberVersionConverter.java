package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.BetreiberVersion;
import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlBetreiberVersionConverter implements SqlResultsetConverter<BetreiberVersion> {
    private final static String[] SELECT_COLS = SqlVersionConverter.SELECT_COLS;


    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM N_BETREIBER_V",
            String.join(",", SELECT_COLS)
        );
    }


    @Override
    @SneakyThrows
    public BetreiberVersion fromResultSet(ResultSet row) {
        return new BetreiberVersion(
            SqlHasIdConverter.getId(row),
            SqlVersionConverter.getElementId(row),
            SqlVersionConverter.getGueltigVon(row),
            SqlVersionConverter.getGueltigBis(row)
        );
    }
}
