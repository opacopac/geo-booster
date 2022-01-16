package com.tschanz.geobooster.zonen_persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.service.SqlStandardReader;
import com.tschanz.geobooster.util.model.KeyValue;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning.service.VersioningHelper;
import com.tschanz.geobooster.versioning_persistence.model.ElementVersionChanges;
import com.tschanz.geobooster.versioning_persistence_sql.service.SqlChangeDetector;
import com.tschanz.geobooster.zonen.model.Zone;
import com.tschanz.geobooster.zonen.model.Zonenplan;
import com.tschanz.geobooster.zonen.model.ZonenplanVersion;
import com.tschanz.geobooster.zonen_persistence.service.ZonePersistence;
import com.tschanz.geobooster.zonen_persistence.service.ZonenplanPersistence;
import com.tschanz.geobooster.zonen_persistence_sql.model.SqlZonenplanElementMapping;
import com.tschanz.geobooster.zonen_persistence_sql.model.SqlZonenplanVersionMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class ZonenplanSqlPersistence implements ZonenplanPersistence {
    private final SqlStandardReader sqlReader;
    private final ZonePersistence zonePersistence;
    private final SqlChangeDetector changeDetector;


    @Override
    public Collection<Zonenplan> readAllElements() {
        return this.readElements(Collections.emptyList());
    }


    @Override
    public Collection<ZonenplanVersion> readAllVersions() {
        var rgaEs = this.readAllElements();

        return this.readAllVersions(rgaEs);
    }


    @Override
    public Collection<ZonenplanVersion> readAllVersions(Collection<Zonenplan> elements) {
        return this.readVersions(Collections.emptyList(), elements);
    }


    @Override
    public ElementVersionChanges<Zonenplan, ZonenplanVersion> findChanges(LocalDateTime changedSince, Collection<Long> currentVersionIds, Collection<Zonenplan> elements) {
        var modifiedDeletedVersionIds = this.changeDetector.findModifiedDeletedIds(SqlZonenplanVersionMapping.TABLE_NAME, changedSince, currentVersionIds);
        var modifiedVersionIds = modifiedDeletedVersionIds.getList1();
        var modifiedVersions = !modifiedVersionIds.isEmpty() ? this.readVersions(modifiedVersionIds, elements) : Collections.<ZonenplanVersion>emptyList();
        var modifiedElementIds = modifiedVersions.stream().map(ZonenplanVersion::getElementId).distinct().collect(Collectors.toList());
        var modifiedElements = !modifiedElementIds.isEmpty() ? this.readElements(modifiedElementIds) : Collections.<Zonenplan>emptyList();

        return new ElementVersionChanges<>(
            modifiedElements,
            modifiedVersions,
            Collections.emptyList(), // ignoring deleted elements
            modifiedDeletedVersionIds.getList2()
        );
    }


    private Collection<Zonenplan> readElements(Collection<Long> elementIds) {
        var mapping = new SqlZonenplanElementMapping(elementIds);

        return this.sqlReader.read(mapping);
    }


    private Collection<ZonenplanVersion> readVersions(Collection<Long> versionIds, Collection<Zonenplan> elements) {
        var zpEMap = VersioningHelper.createIdMap(elements);

        var zoneVs = this.zonePersistence.readVersions(versionIds);
        var zoneVByElementMap = VersioningHelper.createElementIdMap(zoneVs);

        var zoneEIds = versionIds.isEmpty() ? Collections.<Long>emptyList() : zoneVByElementMap.keySet();
        var zoneEs = this.zonePersistence.readElements(zoneEIds);
        var zoneEByZpMap = ArrayHelper.create1toNLookupMap(zoneEs, Zone::getZonenplanId, k -> k);

        var zoneVkIds = this.zonePersistence.readVkIds(versionIds);
        var zoneVkIdsMap = ArrayHelper.create1toNLookupMap(zoneVkIds, KeyValue::getKey, KeyValue::getValue);

        // TODO
        Map<Long, Long> excludeVks = Collections.emptyMap();

        var mapping = new SqlZonenplanVersionMapping(versionIds, zpEMap, zoneEByZpMap, zoneVByElementMap, zoneVkIdsMap, excludeVks);

        return this.sqlReader.read(mapping);
    }
}
