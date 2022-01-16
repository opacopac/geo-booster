package com.tschanz.geobooster.zonen_persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.service.SqlStandardReader;
import com.tschanz.geobooster.util.model.KeyValue;
import com.tschanz.geobooster.versioning_persistence.model.ElementVersionChanges;
import com.tschanz.geobooster.versioning_persistence_sql.service.SqlChangeDetector;
import com.tschanz.geobooster.zonen.model.Zone;
import com.tschanz.geobooster.zonen.model.ZoneVersion;
import com.tschanz.geobooster.zonen_persistence.service.ZonePersistence;
import com.tschanz.geobooster.zonen_persistence_sql.model.SqlZoneElementMapping;
import com.tschanz.geobooster.zonen_persistence_sql.model.SqlZoneVersionMapping;
import com.tschanz.geobooster.zonen_persistence_sql.model.SqlZoneVkIdsMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class ZoneSqlPersistence implements ZonePersistence {
    private final SqlStandardReader sqlReader;
    private final SqlChangeDetector changeDetector;


    @Override
    public Collection<Zone> readAllElements() {
        return this.readElements(Collections.emptyList());
    }


    @Override
    public Collection<ZoneVersion> readAllVersions() {
        return this.readVersions(Collections.emptyList());
    }


    @Override
    public Collection<Zone> readElements(Collection<Long> elementIds) {
        var mapping = new SqlZoneElementMapping(elementIds);

        return this.sqlReader.read(mapping);
    }


    @Override
    public Collection<ZoneVersion> readVersions(Collection<Long> versionIds) {
        var mapping = new SqlZoneVersionMapping(versionIds);

        return this.sqlReader.read(mapping);
    }


    @Override
    public Collection<KeyValue<Long, Long>> readAllVkIds() {
        return this.readVkIds(Collections.emptyList());
    }


    @Override
    public Collection<KeyValue<Long, Long>> readVkIds(Collection<Long> versionIds) {
        var mapping = new SqlZoneVkIdsMapping(versionIds);

        return this.sqlReader.read(mapping);
    }


    @Override
    public ElementVersionChanges<Zone, ZoneVersion> findChanges(LocalDateTime changedSince, Collection<Long> currentVersionIds) {
        var modifiedDeletedVersionIds = this.changeDetector.findModifiedDeletedIds(SqlZoneVersionMapping.TABLE_NAME, changedSince, currentVersionIds);
        var modifiedVersionIds = modifiedDeletedVersionIds.getList1();
        var modifiedVersions = !modifiedVersionIds.isEmpty() ? this.readVersions(modifiedVersionIds) : Collections.<ZoneVersion>emptyList();
        var modifiedElementIds = modifiedVersions.stream().map(ZoneVersion::getElementId).distinct().collect(Collectors.toList());
        var modifiedElements = !modifiedElementIds.isEmpty() ? this.readElements(modifiedElementIds) : Collections.<Zone>emptyList();

        return new ElementVersionChanges<>(
            modifiedElements,
            modifiedVersions,
            Collections.emptyList(), // ignoring deleted elements
            modifiedDeletedVersionIds.getList2()
        );
    }
}
