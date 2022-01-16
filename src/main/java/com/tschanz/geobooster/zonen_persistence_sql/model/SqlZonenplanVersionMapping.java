package com.tschanz.geobooster.zonen_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.versioning.service.VersioningHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdMapping;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionMapping;
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
public class SqlZonenplanVersionMapping implements SqlStandardMapping<ZonenplanVersion, SqlLongFilter, Long> {
    public static String TABLE_NAME = "Z_ZONENPLAN_V";

    private final Collection<Long> versionIds;
    private final Map<Long, Zonenplan> zonenplanMap;
    private final Map<Long, Collection<Zone>> zoneEByZonenplanMap;
    private final Map<Long, Collection<ZoneVersion>> zoneVbyElementMap;
    private final Map<Long, Collection<Long>> zoneVkIds;
    private final Map<Long, Long> excludeVkMap;


    @Override
    public String getTable() {
        return TABLE_NAME;
    }


    @Override
    public String[] getSelectFields() {
        return SqlVersionMapping.SELECT_COLS;
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return SqlLongFilter.createSingleton(SqlHasIdMapping.COL_ID, this.versionIds);
    }


    @SneakyThrows
    @Override
    public ZonenplanVersion fromResultSet(ResultSet row) {
        return this.createZpVersion(
            SqlHasIdMapping.getId(row),
            SqlVersionMapping.getElementId(row),
            SqlVersionMapping.getGueltigVon(row),
            SqlVersionMapping.getGueltigBis(row)
        );
    }


    @Override
    public ZonenplanVersion fromJsonAgg(JsonReader reader) {
        return this.createZpVersion(
            SqlHasIdMapping.getIdFromJsonAgg(reader),
            SqlVersionMapping.getElementIdFromJsonAgg(reader),
            SqlVersionMapping.getGueltigVonFromJsonAgg(reader),
            SqlVersionMapping.getGueltigBisFromJsonAgg(reader)
        );
    }


    private ZonenplanVersion createZpVersion(
        long id,
        long elementId,
        LocalDate gueltigVon,
        LocalDate gueltigBis
    ) {
        var zpE = this.zonenplanMap.get(elementId);
        var zoneEs = this.zoneEByZonenplanMap.get(zpE.getId());

        var vkIds = zoneEs == null ? Collections.<Long>emptyList() : zoneEs.stream()
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
        var zoneVs = this.zoneVbyElementMap.get(zoneE.getId());
        var zoneV = zoneVs != null ? VersioningHelper.filterSingleVersion(zoneVs, date) : null;

        if (zoneV != null && zoneV.getUrsprungsZoneId() > 0) {
            var uZoneVs = this.zoneVbyElementMap.get(zoneV.getUrsprungsZoneId());
            return uZoneVs != null ? VersioningHelper.filterSingleVersion(uZoneVs, date) : null;
        } else {
            return zoneV;
        }
    }
}
