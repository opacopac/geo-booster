package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.netz.model.Verwaltung;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlElementInfoConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Map;


public class SqlVerwaltungElementConverter {
    public final static String COL_CODE = "CODE";
    public final static String COL_IDBETREIBER = "ID_BETREIBER";
    public final static String COL_INFOPLUSTC = "INFO_PLUS_TC";
    public final static String[] ALL_COLS = ArrayHelper.appendTo(SqlElementInfoConverter.ALL_COLS,
        COL_CODE, COL_IDBETREIBER, COL_INFOPLUSTC);


    @SneakyThrows
    public static Verwaltung fromResultSet(ResultSet row, Map<Long, Betreiber> betreiberElementMap) {
        return new Verwaltung(
            SqlElementInfoConverter.fromResultSet(row),
            row.getString(COL_CODE),
            betreiberElementMap.get(row.getLong(COL_IDBETREIBER)),
            row.getString(COL_INFOPLUSTC)
        );
    }
}
