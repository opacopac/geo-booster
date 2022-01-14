package com.tschanz.geobooster.zonen_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardConverter;
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
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class SqlZonenplanVersionConverter implements SqlStandardConverter<ZonenplanVersion, SqlLongFilter, Long> {
    private final Map<Long, Zonenplan> zonenplanMap;
    private final Map<Long, Collection<Zone>> zoneByZonenplanMap;
    private final Map<Long, Collection<ZoneVersion>> zoneVersionMap;
    private final Map<Long, Collection<Long>> zoneVkIds;
    private final Map<Long, Long> excludeVkMap;


    @Override
    public String getTable() {
        return "Z_ZONENPLAN_V";
    }

    @Override
    public String[] getSelectFields() {
        return SqlVersionConverter.SELECT_COLS;
    }

    @Override
    public Collection<SqlLongFilter> getFilters() {
        return Collections.emptyList();
    }


    @SneakyThrows
    @Override
    public ZonenplanVersion fromResultSet(ResultSet row) {
        return this.createZpVersion(
            SqlHasIdConverter.getId(row),
            SqlVersionConverter.getElementId(row),
            SqlVersionConverter.getGueltigVon(row),
            SqlVersionConverter.getGueltigBis(row)
        );
    }


    @Override
    public ZonenplanVersion fromJsonAgg(JsonReader reader) {
        return this.createZpVersion(
            SqlHasIdConverter.getIdFromJsonAgg(reader),
            SqlVersionConverter.getElementIdFromJsonAgg(reader),
            SqlVersionConverter.getGueltigVonFromJsonAgg(reader),
            SqlVersionConverter.getGueltigBisFromJsonAgg(reader)
        );
    }


    private ZonenplanVersion createZpVersion(
        long id,
        long elementId,
        LocalDate gueltigVon,
        LocalDate gueltigBis
    ) {
        var zpE = this.zonenplanMap.get(elementId);
        var zoneEs = this.zoneByZonenplanMap.get(zpE.getId());

        Collection<Long> vkIds = zoneEs == null ? Collections.emptyList() : zoneEs.stream()
            .map(zoneE -> this.getZoneOrUberZoneVersion(zoneE, gueltigBis))
            .filter(Objects::nonNull)
            .map(zoneV -> this.zoneVkIds.get(zoneV.getId()))
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .filter(vkId -> !excludeVkMap.containsKey(vkId))
            .distinct()
            .collect(Collectors.toList());

        return new ZonenplanVersion(
            id,
            elementId,
            gueltigVon,
            gueltigBis,
            vkIds
        );
    }


    private ZoneVersion getZoneOrUberZoneVersion(Zone zoneE, LocalDate date) {
        var zoneVs = this.zoneVersionMap.get(zoneE.getId());
        var zoneV = zoneVs != null ? VersioningHelper.filterSingleVersion(zoneVs, date) : null;

        if (zoneV != null && zoneV.getUrsprungsZoneId() > 0) {
            var uZoneVs = this.zoneVersionMap.get(zoneV.getUrsprungsZoneId());
            return uZoneVs != null ? VersioningHelper.filterSingleVersion(uZoneVs, date) : null;
        } else {
            return zoneV;
        }
    }
}
