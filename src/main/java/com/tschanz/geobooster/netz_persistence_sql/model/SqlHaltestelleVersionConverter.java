package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.geofeature.model.Epsg4326Coordinate;
import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlHaltestelleVersionConverter implements SqlResultsetConverter<HaltestelleVersion> {
    private final static String COL_NAME = "NAME";
    private final static String COL_LAT = "LAT";
    private final static String COL_LNG = "LNG";
    private final static String[] SELECT_COLS = ArrayHelper.appendTo(SqlVersionConverter.SELECT_COLS, COL_NAME, COL_LAT, COL_LNG);


    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM N_HALTESTELLE_V",
            String.join(",", SELECT_COLS)
        );
    }


    @SneakyThrows
    @Override
    public HaltestelleVersion fromResultSet(ResultSet row) {
        return new HaltestelleVersion(
            SqlHasIdConverter.getId(row),
            SqlVersionConverter.getElementId(row),
            SqlVersionConverter.getGueltigVon(row),
            SqlVersionConverter.getGueltigBis(row),
            row.getString(COL_NAME),
            CoordinateConverter.convertToEpsg3857(
                new Epsg4326Coordinate(row.getFloat(COL_LNG), row.getFloat(COL_LAT))
            )
        );
    }
}
