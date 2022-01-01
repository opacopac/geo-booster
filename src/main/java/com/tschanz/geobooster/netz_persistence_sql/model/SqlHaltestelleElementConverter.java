package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlElementConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlHaltestelleElementConverter implements SqlResultsetConverter<Haltestelle> {
    public final static String COL_UIC = "UIC_CODE";
    public final static String[] SELECT_COLS = ArrayHelper.appendTo(SqlElementConverter.SELECT_COLS, COL_UIC);


    @SneakyThrows
    @Override
    public Haltestelle fromResultSet(ResultSet row) {
        return new Haltestelle(
            SqlElementConverter.getId(row),
            row.getInt(COL_UIC)
        );
    }
}
