package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.common.service.ArrayHelper;
import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlElementInfoConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlHaltestelleElementConverter {
    public final static String COL_UIC = "UIC_CODE";
    public final static String[] ALL_COLS = ArrayHelper.appendTo(SqlElementInfoConverter.ALL_COLS, COL_UIC);


    @SneakyThrows
    public static Haltestelle fromResultSet(ResultSet row) {
        return new Haltestelle(
            SqlElementInfoConverter.fromResultSet(row),
            row.getInt(COL_UIC)
        );
    }
}
