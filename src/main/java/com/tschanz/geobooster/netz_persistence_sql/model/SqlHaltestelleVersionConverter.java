package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.geofeature.model.Epsg4326Coordinate;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlHaltestelleVersionConverter implements SqlResultsetConverter<HaltestelleVersion> {
    public final static String COL_NAME = "NAME";
    public final static String COL_LAT = "LAT";
    public final static String COL_LNG = "LNG";
    public final static String[] SELECT_COLS = ArrayHelper.appendTo(SqlVersionConverter.SELECT_COLS, COL_NAME, COL_LAT, COL_LNG);


    @SneakyThrows
    @Override
    public HaltestelleVersion fromResultSet(ResultSet row) {
        return new HaltestelleVersion(
            SqlHasIdConverter.getId(row),
            SqlVersionConverter.getElementId(row),
            SqlVersionConverter.getGueltigVon(row),
            SqlVersionConverter.getGueltigBis(row),
            row.getString(COL_NAME),
            new Epsg4326Coordinate(
                row.getFloat(COL_LNG),
                row.getFloat(COL_LAT)
            )
        );
    }
}
