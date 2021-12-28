package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
public class SqlTarifkanteVersionConverter implements SqlResultsetConverter<TarifkanteVersion> {
    public final static String[] ALL_COLS = SqlVersionConverter.ALL_COLS;
    public final Map<Long, List<Long>> tkVkMap;


    @SneakyThrows
    public TarifkanteVersion fromResultSet(ResultSet row) {
        var id = SqlVersionConverter.getId(row);
        var vkIds = tkVkMap.get(id);
        if (vkIds == null) {
            vkIds = Collections.emptyList();
        }

        return new TarifkanteVersion(
            id,
            SqlVersionConverter.getElementId(row),
            SqlVersionConverter.getGueltigVon(row),
            SqlVersionConverter.getGueltigBis(row),
            vkIds
        );
    }
}
