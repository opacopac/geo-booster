package com.tschanz.geobooster.tarif_persistence_sql.model;

import com.tschanz.geobooster.netz_persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.tarif.model.AwbVersion;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Map;


@RequiredArgsConstructor
public class SqlAwbVersionConverter implements SqlResultsetConverter<AwbVersion> {
    public final static String[] SELECT_COLS = SqlVersionConverter.SELECT_COLS;

    private final Map<Long, Collection<Long>> includeVkMap;
    private final Map<Long, Collection<Long>> excludeVkMap;
    private final Map<Long, Collection<Long>> includeTkMap;
    private final Map<Long, Collection<Long>> excludeTkMap;
    private final Map<Long, Collection<Long>> includeVerwMap;
    private final Map<Long, Collection<Long>> includeZpMap;
    private final Map<Long, Collection<Long>> includeRgaMap;


    @Override
    @SneakyThrows
    public AwbVersion fromResultSet(ResultSet row) {
        var id = SqlHasIdConverter.getId(row);

        return new AwbVersion(
            id,
            SqlVersionConverter.getElementId(row),
            SqlVersionConverter.getGueltigVon(row),
            SqlVersionConverter.getGueltigBis(row),
            this.includeVkMap.get(id),
            this.excludeVkMap.get(id),
            this.includeTkMap.get(id),
            this.excludeTkMap.get(id),
            this.includeVerwMap.get(id),
            this.includeZpMap.get(id),
            this.includeRgaMap.get(id)
        );
    }
}
