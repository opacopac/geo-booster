package com.tschanz.geobooster.rtm_persistence.service;

import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabeVersion;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlHaltestelleWegangabeVersionConverter implements SqlResultsetConverter<HaltestelleWegangabeVersion> {
    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM R_HALTESTELLEN_WEGANGABE_V",
            String.join(",", SqlVersionConverter.SELECT_COLS)
        );
    }


    @SneakyThrows
    @Override
    public HaltestelleWegangabeVersion fromResultSet(ResultSet row) {
        return new HaltestelleWegangabeVersion(
            SqlHasIdConverter.getId(row),
            SqlVersionConverter.getElementId(row),
            SqlVersionConverter.getGueltigVon(row),
            SqlVersionConverter.getGueltigBis(row)
        );
    }
}
