package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.Verwaltung;
import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlElementConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlVerwaltungElementConverter implements SqlResultsetConverter<Verwaltung> {
    private final static String COL_CODE = "CODE";
    private final static String COL_IDBETREIBER = "ID_BETREIBER";
    private final static String COL_INFOPLUSTC = "INFO_PLUS_TC";
    private final static String[] SELECT_COLS = ArrayHelper.appendTo(SqlElementConverter.SELECT_COLS, COL_CODE, COL_IDBETREIBER, COL_INFOPLUSTC);


    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM N_VERWALTUNG_E",
            String.join(",", SELECT_COLS)
        );
    }


    @SneakyThrows
    @Override
    public Verwaltung fromResultSet(ResultSet row) {
        return new Verwaltung(
            SqlHasIdConverter.getId(row),
            row.getString(COL_CODE),
            row.getLong(COL_IDBETREIBER),
            row.getString(COL_INFOPLUSTC)
        );
    }
}
