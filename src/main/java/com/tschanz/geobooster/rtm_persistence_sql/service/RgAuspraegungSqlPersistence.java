package com.tschanz.geobooster.rtm_persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.service.SqlStandardReader;
import com.tschanz.geobooster.rtm.model.RgAuspraegung;
import com.tschanz.geobooster.rtm.model.RgAuspraegungVersion;
import com.tschanz.geobooster.rtm_persistence.service.RgAuspraegungPersistence;
import com.tschanz.geobooster.rtm_persistence_sql.model.SqlRgAuspraegungElementMapping;
import com.tschanz.geobooster.rtm_persistence_sql.model.SqlRgAuspraegungVersionMapping;
import com.tschanz.geobooster.versioning_persistence.model.ElementVersionChanges;
import com.tschanz.geobooster.versioning_persistence_sql.service.SqlChangeDetector;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class RgAuspraegungSqlPersistence implements RgAuspraegungPersistence {
    private final SqlStandardReader sqlReader;
    private final SqlChangeDetector changeDetector;


    @Override
    public Collection<RgAuspraegung> readAllElements() {
        return this.readElements(Collections.emptyList());
    }


    @Override
    public Collection<RgAuspraegungVersion> readAllVersions() {
        return this.readVersions(Collections.emptyList());
    }


    @Override
    public ElementVersionChanges<RgAuspraegung, RgAuspraegungVersion> findChanges(LocalDateTime changedSince, Collection<Long> currentVersionIds) {
        var changes = this.changeDetector.findModifiedDeletedChanges(SqlRgAuspraegungVersionMapping.TABLE_NAME, changedSince, currentVersionIds);
        var modifiedVersionIds = changes.getModifiedIds();
        var modifiedVersions = !modifiedVersionIds.isEmpty() ? this.readVersions(modifiedVersionIds) : Collections.<RgAuspraegungVersion>emptyList();
        var modifiedElementIds = modifiedVersions.stream().map(RgAuspraegungVersion::getElementId).distinct().collect(Collectors.toList());
        var modifiedElements = !modifiedElementIds.isEmpty() ? this.readElements(modifiedElementIds) : Collections.<RgAuspraegung>emptyList();

        return new ElementVersionChanges<>(
            modifiedElements,
            modifiedVersions,
            Collections.emptyList(), // ignoring deleted elements
            changes.getDeletedIds()
        );
    }


    private Collection<RgAuspraegung> readElements(Collection<Long> filterElementIds) {
        var mapping = new SqlRgAuspraegungElementMapping(filterElementIds);

        return this.sqlReader.read(mapping);
    }


    private Collection<RgAuspraegungVersion> readVersions(Collection<Long> filterVersionIds) {
        var mapping = new SqlRgAuspraegungVersionMapping(filterVersionIds);

        return this.sqlReader.read(mapping);
    }
}
