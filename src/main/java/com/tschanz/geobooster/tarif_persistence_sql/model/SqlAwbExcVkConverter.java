package com.tschanz.geobooster.tarif_persistence_sql.model;

import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.util.model.KeyValue;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;


@RequiredArgsConstructor
public class SqlAwbExcVkConverter implements SqlResultsetConverter<KeyValue<Long, Long>> {
    private final static String COL_ID_ANWBER_V = "ID_ANWBER_V";
    private final static String COL_ID_KANTEN_AUSPRAEGUNG_E = "ID_KANTEN_AUSPRAEGUNG_E";
    private final static String[] SELECT_COLS = {COL_ID_ANWBER_V, COL_ID_KANTEN_AUSPRAEGUNG_E};


    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM T_ANWBER_X_EXCLUDE_KANTEN",
            String.join(",", SELECT_COLS)
        );
    }


    @Override
    @SneakyThrows
    public KeyValue<Long, Long> fromResultSet(ResultSet row) {
        return new KeyValue<>(
            row.getLong(COL_ID_ANWBER_V),
            row.getLong(COL_ID_KANTEN_AUSPRAEGUNG_E)
        );
    }
}