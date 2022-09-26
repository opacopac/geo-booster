package com.tschanz.geobooster.netz_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.geofeature.model.Epsg4326Coordinate;
import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning.model.Pflegestatus;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdMapping;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionMapping;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;


public class SqlHaltestelleVersionMapping implements SqlStandardMapping<HaltestelleVersion, SqlLongFilter, Long> {
    private final static String COL_NAME = "NAME";
    private final static String COL_LNG = "LNG";
    private final static String COL_LAT = "LAT";


    @Override
    public String getTable() {
        return "N_HALTESTELLE_V";
    }


    @Override
    public String[] getSelectFields() {
        return ArrayHelper.appendTo(SqlVersionMapping.SELECT_COLS, COL_NAME, COL_LNG, COL_LAT);
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return Collections.emptyList();
    }


    @Override
    @SneakyThrows
    public HaltestelleVersion fromResultSet(ResultSet row) {
        return new HaltestelleVersion(
            SqlHasIdMapping.getId(row),
            SqlVersionMapping.getElementId(row),
            SqlVersionMapping.getGueltigVon(row),
            SqlVersionMapping.getGueltigBis(row),
            Pflegestatus.PRODUKTIV,
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
            SqlHasIdMapping.getIdFromJsonAgg(reader),
            SqlVersionMapping.getElementIdFromJsonAgg(reader),
            SqlVersionMapping.getGueltigVonFromJsonAgg(reader),
            SqlVersionMapping.getGueltigBisFromJsonAgg(reader),
            Pflegestatus.PRODUKTIV,
            reader.nextString(),
            CoordinateConverter.convertToEpsg3857(
                new Epsg4326Coordinate(reader.nextDouble(), reader.nextDouble())
            )
        );
    }
}
