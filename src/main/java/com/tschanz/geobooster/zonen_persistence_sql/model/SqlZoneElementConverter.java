package com.tschanz.geobooster.zonen_persistence_sql.model;

import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlElementConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.zonen.model.Zone;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;


@RequiredArgsConstructor
public class SqlZoneElementConverter implements SqlResultsetConverter<Zone> {
    private final static String COL_ID_ZONENPLAN = "ID_ZONENPLAN";
    private final static String[] SELECT_COLS = ArrayHelper.appendTo(SqlElementConverter.SELECT_COLS, COL_ID_ZONENPLAN);


    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM Z_ZONE_E",
            String.join(",", SELECT_COLS)
        );
    }


    @Override
    @SneakyThrows
    public Zone fromResultSet(ResultSet row) {
        return new Zone(
            SqlHasIdConverter.getId(row),
            row.getLong(COL_ID_ZONENPLAN)
        );
    }
}
