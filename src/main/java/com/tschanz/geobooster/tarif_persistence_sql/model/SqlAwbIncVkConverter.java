package com.tschanz.geobooster.tarif_persistence_sql.model;

import com.tschanz.geobooster.netz_persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.util.model.KeyValue;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;


@RequiredArgsConstructor
public class SqlAwbIncVkConverter implements SqlResultsetConverter<KeyValue<Long, Long>> {
    public final static String COL_ID_ANWBER_V = "ID_ANWBER_V";
    public final static String COL_ID_KANTEN_AUSPRAEGUNG_E = "ID_KANTEN_AUSPRAEGUNG_E";
    public final static String[] SELECT_COLS = {COL_ID_ANWBER_V, COL_ID_KANTEN_AUSPRAEGUNG_E};


    @Override
    @SneakyThrows
    public KeyValue<Long, Long> fromResultSet(ResultSet row) {
        return new KeyValue<>(
            row.getLong(COL_ID_ANWBER_V),
            row.getLong(COL_ID_KANTEN_AUSPRAEGUNG_E)
        );
    }
}
