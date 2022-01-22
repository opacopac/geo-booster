package com.tschanz.geobooster.rtm_persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.service.SqlStandardReader;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabe;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabeVersion;
import com.tschanz.geobooster.rtm_persistence.service.HaltestelleWegangabePersistence;
import com.tschanz.geobooster.rtm_persistence_sql.model.SqlHaltestelleWegangabeElementMapping;
import com.tschanz.geobooster.rtm_persistence_sql.model.SqlHaltestelleWegangabeVersionMapping;
import com.tschanz.geobooster.versioning_persistence.model.ElementVersionChanges;
import com.tschanz.geobooster.versioning_persistence_sql.service.SqlChangeDetector;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class HaltestelleWegangabeSqlPersistence implements HaltestelleWegangabePersistence {
    private final SqlStandardReader sqlReader;
    private final SqlChangeDetector changeDetector;


    @Override
    @SneakyThrows
    public Collection<HaltestelleWegangabe> readAllElements() {
        return this.readElements(Collections.emptyList());
    }


    @Override
    @SneakyThrows
    public Collection<HaltestelleWegangabeVersion> readAllVersions() {
        return this.readVersions(Collections.emptyList());
    }


    @Override
    public ElementVersionChanges<HaltestelleWegangabe, HaltestelleWegangabeVersion> findChanges(LocalDateTime changedSince, Collection<Long> currentVersionIds) {
        var changes = this.changeDetector.findModifiedDeletedChanges(SqlHaltestelleWegangabeVersionMapping.TABLE_NAME, changedSince, currentVersionIds);
        var modifiedVersionIds = changes.getModifiedIds();
        var modifiedVersions = !modifiedVersionIds.isEmpty() ? this.readVersions(modifiedVersionIds) : Collections.<HaltestelleWegangabeVersion>emptyList();
        var modifiedElementIds = modifiedVersions.stream().map(HaltestelleWegangabeVersion::getElementId).distinct().collect(Collectors.toList());
        var modifiedElements = !modifiedElementIds.isEmpty() ? this.readElements(modifiedElementIds) : Collections.<HaltestelleWegangabe>emptyList();

        return new ElementVersionChanges<>(
            modifiedElements,
            modifiedVersions,
            Collections.emptyList(), // ignoring deleted elements
            changes.getDeletedIds()
        );
    }


    private Collection<HaltestelleWegangabe> readElements(Collection<Long> filterElementIds) {
        var mapping = new SqlHaltestelleWegangabeElementMapping(filterElementIds);

        return this.sqlReader.read(mapping);
    }


    private Collection<HaltestelleWegangabeVersion> readVersions(Collection<Long> filterVersionIds) {
        var mapping = new SqlHaltestelleWegangabeVersionMapping(filterVersionIds);

        return this.sqlReader.read(mapping);
    }
}
