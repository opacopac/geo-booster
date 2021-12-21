package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.common.service.ArrayHelper;
import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.netz.model.Verkehrskante;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlElementInfoConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Map;


public class SqlVerkehrskanteElementConverter {
    public final static String COL_HST1 = "ID_HS_ELEMENT_1";
    public final static String COL_HST2 = "ID_HS_ELEMENT_2";
    public final static String[] ALL_COLS = ArrayHelper.appendTo(SqlElementInfoConverter.ALL_COLS, COL_HST1, COL_HST2);


    @SneakyThrows
    public static Verkehrskante fromResultSet(ResultSet row, Map<Long, Haltestelle> haltestelleMap) {
        return new Verkehrskante(
            SqlElementInfoConverter.fromResultSet(row),
            haltestelleMap.get(row.getLong(COL_HST1)),
            haltestelleMap.get(row.getLong(COL_HST2))
        );
    }
}
