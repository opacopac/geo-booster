package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.netz.model.BetreiberVersion;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionInfoConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Map;


public class SqlBetreiberVersionConverter {
    public final static String[] ALL_COLS = SqlVersionInfoConverter.ALL_COLS;


    @SneakyThrows
    public static BetreiberVersion fromResultSet(ResultSet row, Map<Long, Betreiber> betreiberElementMap) {
        var betreiberV = new BetreiberVersion(
            SqlVersionInfoConverter.fromResultSet(row, betreiberElementMap)
        );
        betreiberV.getVersionInfo().getElement().getElementInfo().getVersions().add(betreiberV);

        return betreiberV;
    }
}
