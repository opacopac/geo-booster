package com.tschanz.geobooster.rtm_persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.service.SqlStandardReader;
import com.tschanz.geobooster.rtm.model.RgKorridor;
import com.tschanz.geobooster.rtm.model.RgKorridorVersion;
import com.tschanz.geobooster.rtm_persistence.service.RgKorridorPersistence;
import com.tschanz.geobooster.rtm_persistence_sql.model.SqlRgKorridorElementMapping;
import com.tschanz.geobooster.rtm_persistence_sql.model.SqlRgKorridorTkIdsMapping;
import com.tschanz.geobooster.rtm_persistence_sql.model.SqlRgKorridorVersionMapping;
import com.tschanz.geobooster.util.model.KeyValue;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence.model.ElementVersionChanges;
import com.tschanz.geobooster.versioning_persistence_sql.service.SqlChangeDetector;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class RgKorridorSqlPersistence implements RgKorridorPersistence {
    private final SqlStandardReader sqlReader;
    private final SqlChangeDetector changeDetector;


    @Override
    public Collection<RgKorridor> readAllElements() {
        return this.readElements(Collections.emptyList());
    }


    @Override
    public Collection<RgKorridorVersion> readAllVersions() {
        return this.readVersions(Collections.emptyList());
    }


    @Override
    public ElementVersionChanges<RgKorridor, RgKorridorVersion> findChanges(LocalDateTime changedSince, Collection<Long> currentVersionIds) {
        var modifiedDeletedVersionIds = this.changeDetector.findModifiedDeletedIds(SqlRgKorridorVersionMapping.TABLE_NAME, changedSince, currentVersionIds);
        var modifiedVersionIds = modifiedDeletedVersionIds.getList1();
        var modifiedVersions = !modifiedVersionIds.isEmpty() ? this.readVersions(modifiedVersionIds) : Collections.<RgKorridorVersion>emptyList();
        var modifiedElementIds = modifiedVersions.stream().map(RgKorridorVersion::getElementId).distinct().collect(Collectors.toList());
        var modifiedElements = !modifiedElementIds.isEmpty() ? this.readElements(modifiedElementIds) : Collections.<RgKorridor>emptyList();

        return new ElementVersionChanges<>(
            modifiedElements,
            modifiedVersions,
            Collections.emptyList(), // ignoring deleted elements
            modifiedDeletedVersionIds.getList2()
        );
    }


    private Collection<RgKorridor> readElements(Collection<Long> elementIds) {
        var mapping = new SqlRgKorridorElementMapping(elementIds);

        return this.sqlReader.read(mapping);
    }


    private Collection<RgKorridorVersion> readVersions(Collection<Long> versionIds) {
        var korrTkMap = this.readKorridorTkMap();
        var mapping = new SqlRgKorridorVersionMapping(korrTkMap, versionIds);

        return this.sqlReader.read(mapping);
    }


    private Map<Long, Collection<Long>> readKorridorTkMap() {
        var mapping = new SqlRgKorridorTkIdsMapping();
        var rgKorrTkIds = this.sqlReader.read(mapping);

        return ArrayHelper.create1toNLookupMap(rgKorrTkIds, KeyValue::getKey, KeyValue::getValue);
    }
}
