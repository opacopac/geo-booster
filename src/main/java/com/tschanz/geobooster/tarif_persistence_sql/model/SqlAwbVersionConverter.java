package com.tschanz.geobooster.tarif_persistence_sql.model;

import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.tarif.model.AwbVersion;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;


@RequiredArgsConstructor
public class SqlAwbVersionConverter implements SqlResultsetConverter<AwbVersion> {
    private final Map<Long, Collection<Long>> includeVkMap;
    private final Map<Long, Collection<Long>> excludeVkMap;
    private final Map<Long, Collection<Long>> includeTkMap;
    private final Map<Long, Collection<Long>> excludeTkMap;
    private final Map<Long, Collection<Long>> includeVerwMap;
    private final Map<Long, Collection<Long>> includeZpMap;
    private final Map<Long, Collection<Long>> includeRgaMap;


    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM T_ANWBER_V",
            String.join(",", SqlVersionConverter.SELECT_COLS)
        );
    }


    @Override
    @SneakyThrows
    public AwbVersion fromResultSet(ResultSet row) {
        var id = SqlHasIdConverter.getId(row);

        return new AwbVersion(
            id,
            SqlVersionConverter.getElementId(row),
            SqlVersionConverter.getGueltigVon(row),
            SqlVersionConverter.getGueltigBis(row),
            this.includeVkMap.getOrDefault(id, Collections.emptyList()),
            this.excludeVkMap.getOrDefault(id, Collections.emptyList()),
            this.includeTkMap.getOrDefault(id, Collections.emptyList()),
            this.excludeTkMap.getOrDefault(id, Collections.emptyList()),
            this.includeVerwMap.getOrDefault(id, Collections.emptyList()),
            this.includeZpMap.getOrDefault(id, Collections.emptyList()),
            this.includeRgaMap.getOrDefault(id, Collections.emptyList())
        );
    }
}
