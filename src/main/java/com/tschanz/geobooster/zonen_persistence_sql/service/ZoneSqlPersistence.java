package com.tschanz.geobooster.zonen_persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.service.SqlStandardReader;
import com.tschanz.geobooster.util.model.Tuple2;
import com.tschanz.geobooster.versioning_persistence.model.ElementVersionChanges;
import com.tschanz.geobooster.versioning_persistence_sql.service.SqlChangeDetector;
import com.tschanz.geobooster.zonen.model.Zone;
import com.tschanz.geobooster.zonen.model.ZoneVersion;
import com.tschanz.geobooster.zonen.model.ZoneVkZuordnung;
import com.tschanz.geobooster.zonen_persistence.service.ZonePersistence;
import com.tschanz.geobooster.zonen_persistence_sql.model.SqlZoneElementMapping;
import com.tschanz.geobooster.zonen_persistence_sql.model.SqlZoneVersionMapping;
import com.tschanz.geobooster.zonen_persistence_sql.model.SqlZoneVkZuordnungMapping;
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
    public Collection<ZoneVkZuordnung> readAllZoneVkZuordnung() {
        return this.readZoneVkZuordnungen(Collections.emptyList());
    }


    @Override
    public ElementVersionChanges<Zone, ZoneVersion> findZoneVersionChanges(LocalDateTime changedSince, Collection<Long> currentVersionIds) {
        var changes = this.changeDetector.findModifiedDeletedChanges(SqlZoneVersionMapping.TABLE_NAME, changedSince, currentVersionIds);
        var modifiedVersionIds = changes.getModifiedIds();
        var modifiedVersions = !modifiedVersionIds.isEmpty() ? this.readVersions(modifiedVersionIds) : Collections.<ZoneVersion>emptyList();
        var modifiedElementIds = modifiedVersions.stream().map(ZoneVersion::getElementId).distinct().collect(Collectors.toList());
        var modifiedElements = !modifiedElementIds.isEmpty() ? this.readElements(modifiedElementIds) : Collections.<Zone>emptyList();

        return new ElementVersionChanges<>(
            modifiedElements,
            modifiedVersions,
            Collections.emptyList(), // ignoring deleted elements
            changes.getDeletedIds()
        );
    }

    @Override
    public Tuple2<Collection<ZoneVkZuordnung>, Collection<Long>> findZoneVkZuordnungChanges(LocalDateTime changedSince, Collection<Long> currentIds) {
        var changes = this.changeDetector.findModifiedDeletedChanges(SqlZoneVkZuordnungMapping.TABLE_NAME, changedSince, currentIds);
        var modifiedIds = changes.getModifiedIds();
        var deletedIds = changes.getDeletedIds();

        var modifiedZoneVkZuordnungen = !modifiedIds.isEmpty() ? this.readZoneVkZuordnungen(modifiedIds) : Collections.<ZoneVkZuordnung>emptyList();

        return new Tuple2<>(
            modifiedZoneVkZuordnungen,
            deletedIds
        );
    }


    private Collection<Zone> readElements(Collection<Long> filterElementIds) {
        var mapping = new SqlZoneElementMapping(filterElementIds);

        return this.sqlReader.read(mapping);
    }


    private Collection<ZoneVersion> readVersions(Collection<Long> filterVersionIds) {
        var mapping = new SqlZoneVersionMapping(filterVersionIds);

        return this.sqlReader.read(mapping);
    }


    private Collection<ZoneVkZuordnung> readZoneVkZuordnungen(Collection<Long> filterIds) {
        var mapping = new SqlZoneVkZuordnungMapping(filterIds);

        return this.sqlReader.read(mapping);
    }
}
