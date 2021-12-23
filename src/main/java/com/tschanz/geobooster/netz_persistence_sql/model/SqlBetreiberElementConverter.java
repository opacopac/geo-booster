package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlElementInfoConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlBetreiberElementConverter {
    public final static String COL_NAME = "NAME";
    public final static String COL_ABK = "ABKUERZUNG";
    public final static String[] ALL_COLS = ArrayHelper.appendTo(SqlElementInfoConverter.ALL_COLS, COL_NAME, COL_ABK);


    @SneakyThrows
    public static Betreiber fromResultSet(ResultSet row) {
        return new Betreiber(
            SqlElementInfoConverter.fromResultSet(row),
            row.getString(COL_NAME),
            row.getString(COL_ABK)
        );
    }
}
