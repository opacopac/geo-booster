package com.tschanz.geobooster.zonen_persistence_sql.model;

import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.versioning.service.VersioningHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import com.tschanz.geobooster.zonen.model.Zone;
import com.tschanz.geobooster.zonen.model.ZoneVersion;
import com.tschanz.geobooster.zonen.model.Zonenplan;
import com.tschanz.geobooster.zonen.model.ZonenplanVersion;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class SqlZonenplanVersionConverter implements SqlResultsetConverter<ZonenplanVersion> {
    private final Map<Long, Zonenplan> zonenplanMap;
    private final Map<Long, Collection<Zone>> zoneByZonenplanMap;
    private final Map<Long, Collection<ZoneVersion>> zoneVesionMap;
    private final Map<Long, Collection<Long>> zoneVkIds;


    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM Z_ZONENPLAN_V",
            String.join(",", SqlVersionConverter.SELECT_COLS)
        );
    }


    @SneakyThrows
    @Override
    public ZonenplanVersion fromResultSet(ResultSet row) {
        var gueltigBis = SqlVersionConverter.getGueltigBis(row);
        var zpEId = SqlVersionConverter.getElementId(row);
        var zpE = this.zonenplanMap.get(zpEId);
        var zoneEs = this.zoneByZonenplanMap.get(zpE.getId());

        Collection<Long> vkIds = zoneEs == null ? Collections.emptyList() : zoneEs.stream()
            .map(zoneE -> {
                var zoneVs = this.zoneVesionMap.get(zoneE.getId());
                return zoneVs != null ? VersioningHelper.filterSingleVersion(zoneVs, gueltigBis) : null;
            })
            .filter(Objects::nonNull)
            .map(zoneV -> this.zoneVkIds.get(zoneV.getId()))
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

        return new ZonenplanVersion(
            SqlHasIdConverter.getId(row),
            zpEId,
            SqlVersionConverter.getGueltigVon(row),
            gueltigBis,
            vkIds
        );
    }
}
