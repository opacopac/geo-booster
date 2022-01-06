package com.tschanz.geobooster.tarif_persistence_sql.model;

import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.tarif.model.Awb;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlElementConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlAwbElementConverter implements SqlResultsetConverter<Awb> {
    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM T_ANWBER_E",
            String.join(",", SqlElementConverter.SELECT_COLS)
        );
    }


    @Override
    @SneakyThrows
    public Awb fromResultSet(ResultSet row) {
        return new Awb(
            SqlHasIdConverter.getId(row)
        );
    }
}
