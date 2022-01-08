package com.tschanz.geobooster.zonen_persistence_sql.model;

import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.util.model.KeyValue;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;


@RequiredArgsConstructor
public class SqlZoneVkIdsConverter implements SqlResultsetConverter<KeyValue<Long, Long>> {
    private final static String COL_ID_ZONE_VERSION = "ID_ZONE_VERSION";
    private final static String COL_ID_KANTE_ELEMENT = "ID_KANTE_ELEMENT";
    private final static String[] SELECT_COLS = {COL_ID_ZONE_VERSION, COL_ID_KANTE_ELEMENT};


    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM Z_ZONE_KANTE_ZUORDNUNG",
            String.join(",", SELECT_COLS)
        );
    }


    @Override
    @SneakyThrows
    public KeyValue<Long, Long> fromResultSet(ResultSet row) {
        return new KeyValue<>(
            row.getLong(COL_ID_ZONE_VERSION),
            row.getLong(COL_ID_KANTE_ELEMENT)
        );
    }
}
