package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.geofeature.model.Epsg4326Coordinate;
import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionInfoConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Map;


public class SqlHaltestelleVersionConverter {
    public final static String COL_NAME = "NAME";
    public final static String COL_LAT = "LAT";
    public final static String COL_LNG = "LNG";
    public final static String[] ALL_COLS = ArrayHelper.appendTo(SqlVersionInfoConverter.ALL_COLS, COL_NAME, COL_LAT, COL_LNG);


    @SneakyThrows
    public static HaltestelleVersion fromResultSet(ResultSet row, Map<Long, Haltestelle> hstElementMap) {
        var hstV = new HaltestelleVersion(
            SqlVersionInfoConverter.fromResultSet(row, hstElementMap),
            row.getString(COL_NAME),
            new Epsg4326Coordinate(
                row.getFloat(COL_LNG),
                row.getFloat(COL_LAT)
            )
        );
        hstV.getVersionInfo().getElement().getElementInfo().getVersions().add(hstV);

        return hstV;
    }
}
