package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.util.model.KeyValue;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class SqlTkVkConverter implements SqlResultsetConverter<KeyValue<Long, Long>> {
    private final static String COL_ID_TARIFKANTE_V = "ID_TARIFKANTE_V";
    private final static String COL_ID_VERKEHRS_KANTE_E = "ID_VERKEHRS_KANTE_E";
    private final static String[] SELECT_COLS = {COL_ID_TARIFKANTE_V, COL_ID_VERKEHRS_KANTE_E};

    private final List<Long> filterTkVIds;


    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM N_TARIFKANTE_X_N_VERK_KANTE_E %s",
            String.join(",", SELECT_COLS),
            this.filterTkVIds != null ? String.format(
                " WHERE ID_TARIFKANTE_V IN (%s)",
                filterTkVIds.stream().map(Object::toString).collect(Collectors.joining(","))
            ) : ""
        );
    }


    @Override
    @SneakyThrows
    public KeyValue<Long, Long> fromResultSet(ResultSet row) {
        return new KeyValue<>(
            row.getLong(COL_ID_TARIFKANTE_V),
            row.getLong(COL_ID_VERKEHRS_KANTE_E)
        );
    }
}
