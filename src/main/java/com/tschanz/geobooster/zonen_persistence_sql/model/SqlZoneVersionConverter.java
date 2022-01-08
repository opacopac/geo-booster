package com.tschanz.geobooster.zonen_persistence_sql.model;

import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import com.tschanz.geobooster.zonen.model.ZoneVersion;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlZoneVersionConverter implements SqlResultsetConverter<ZoneVersion> {
    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM Z_ZONE_V",
            String.join(",", SqlVersionConverter.SELECT_COLS)
        );
    }


    @SneakyThrows
    @Override
    public ZoneVersion fromResultSet(ResultSet row) {
        return new ZoneVersion(
            SqlHasIdConverter.getId(row),
            SqlVersionConverter.getElementId(row),
            SqlVersionConverter.getGueltigVon(row),
            SqlVersionConverter.getGueltigBis(row)
        );
    }
}