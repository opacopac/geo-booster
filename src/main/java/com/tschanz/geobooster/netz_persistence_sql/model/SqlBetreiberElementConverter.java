package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlElementConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlBetreiberElementConverter implements SqlResultsetConverter<Betreiber> {
    public final static String COL_NAME = "NAME";
    public final static String COL_ABK = "ABKUERZUNG";
    public final static String[] ALL_COLS = ArrayHelper.appendTo(SqlElementConverter.ALL_COLS, COL_NAME, COL_ABK);


    @SneakyThrows
    @Override
    public Betreiber fromResultSet(ResultSet row) {
        return new Betreiber(
            SqlElementConverter.getId(row),
            row.getString(COL_NAME),
            row.getString(COL_ABK)
        );
    }
}
