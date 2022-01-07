package com.tschanz.geobooster.rtm_persistence_sql.model;

import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.rtm.model.RgKorridorVersion;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlRgKorridorVersionConverter implements SqlResultsetConverter<RgKorridorVersion> {
    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM R_RELATIONSKORRIDOR_V",
            String.join(",", SqlVersionConverter.SELECT_COLS)
        );
    }


    @SneakyThrows
    @Override
    public RgKorridorVersion fromResultSet(ResultSet row) {
        return new RgKorridorVersion(
            SqlHasIdConverter.getId(row),
            SqlVersionConverter.getElementId(row),
            SqlVersionConverter.getGueltigVon(row),
            SqlVersionConverter.getGueltigBis(row)
        );
    }
}
