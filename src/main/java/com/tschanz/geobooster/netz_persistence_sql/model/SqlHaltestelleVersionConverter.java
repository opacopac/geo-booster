package com.tschanz.geobooster.netz_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.geofeature.model.Epsg4326Coordinate;
import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardConverter;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;


public class SqlHaltestelleVersionConverter implements SqlStandardConverter<HaltestelleVersion, SqlLongFilter, Long> {
    private final static String COL_NAME = "NAME";
    private final static String COL_LNG = "LNG";
    private final static String COL_LAT = "LAT";


    @Override
    public String getTable() {
        return "N_HALTESTELLE_V";
    }


    @Override
    public String[] getSelectFields() {
        return ArrayHelper.appendTo(SqlVersionConverter.SELECT_COLS, COL_NAME, COL_LNG, COL_LAT);
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return Collections.emptyList();
    }


    @Override
    @SneakyThrows
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


    @Override
    @SneakyThrows
    public HaltestelleVersion fromJsonAgg(JsonReader reader) {
        return new HaltestelleVersion(
            SqlHasIdConverter.getIdFromJsonAgg(reader),
            SqlVersionConverter.getElementIdFromJsonAgg(reader),
            SqlVersionConverter.getGueltigVonFromJsonAgg(reader),
            SqlVersionConverter.getGueltigBisFromJsonAgg(reader),
            reader.nextString(),
            CoordinateConverter.convertToEpsg3857(
                new Epsg4326Coordinate(reader.nextDouble(), reader.nextDouble())
            )
        );
    }
}
