package com.tschanz.geobooster.rtm_persistence_sql.model;

import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabe;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlElementConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;


@RequiredArgsConstructor
public class SqlHaltestelleWegangabeElementConverter implements SqlResultsetConverter<HaltestelleWegangabe> {
    private final static String COL_CODE = "CODE";
    private final static String[] SELECT_COLS = ArrayHelper.appendTo(SqlElementConverter.SELECT_COLS, COL_CODE);


    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM R_HALTESTELLEN_WEGANGABE_E",
            String.join(",", SELECT_COLS)
        );
    }


    @Override
    @SneakyThrows
    public HaltestelleWegangabe fromResultSet(ResultSet row) {
        return new HaltestelleWegangabe(
            SqlHasIdConverter.getId(row),
            row.getInt(COL_CODE)
        );
    }
}
