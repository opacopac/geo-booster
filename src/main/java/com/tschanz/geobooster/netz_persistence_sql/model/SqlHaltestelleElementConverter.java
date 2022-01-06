package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlElementConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlHaltestelleElementConverter implements SqlResultsetConverter<Haltestelle> {
    private final static String COL_UIC = "UIC_CODE";
    private final static String[] SELECT_COLS = ArrayHelper.appendTo(SqlElementConverter.SELECT_COLS, COL_UIC);


    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM N_HALTESTELLE_E",
            String.join(",", SELECT_COLS)
        );
    }


    @SneakyThrows
    @Override
    public Haltestelle fromResultSet(ResultSet row) {
        return new Haltestelle(
            SqlHasIdConverter.getId(row),
            row.getInt(COL_UIC)
        );
    }
}
