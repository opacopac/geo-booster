package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlElementConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlBetreiberElementConverter implements SqlResultsetConverter<Betreiber> {
    private final static String COL_NAME = "NAME";
    private final static String COL_ABK = "ABKUERZUNG";
    private final static String[] SELECT_COLS = ArrayHelper.appendTo(SqlElementConverter.SELECT_COLS, COL_NAME, COL_ABK);


    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM N_BETREIBER_E",
            String.join(",", SELECT_COLS)
        );
    }


    @Override
    @SneakyThrows
    public Betreiber fromResultSet(ResultSet row) {
        return new Betreiber(
            SqlHasIdConverter.getId(row),
            row.getString(COL_NAME),
            row.getString(COL_ABK)
        );
    }
}
