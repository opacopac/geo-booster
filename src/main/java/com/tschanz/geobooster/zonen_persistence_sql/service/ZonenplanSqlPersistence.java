package com.tschanz.geobooster.zonen_persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.service.SqlStandardReader;
import com.tschanz.geobooster.versioning_persistence.model.ElementVersionChanges;
import com.tschanz.geobooster.versioning_persistence_sql.service.SqlChangeDetector;
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
        return this.readVersions(Collections.emptyList());
    }


    @Override
    public ElementVersionChanges<Zonenplan, ZonenplanVersion> findChanges(LocalDateTime changedSince, Collection<Long> currentVersionIds) {
        var modifiedDeletedVersionIds = this.changeDetector.findModifiedDeletedIds(SqlZonenplanVersionMapping.TABLE_NAME, changedSince, currentVersionIds);
        var modifiedVersionIds = modifiedDeletedVersionIds.getList1();
        var modifiedVersions = !modifiedVersionIds.isEmpty() ? this.readVersions(modifiedVersionIds) : Collections.<ZonenplanVersion>emptyList();
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


    private Collection<ZonenplanVersion> readVersions(Collection<Long> filterVersionIds) {
        var mapping = new SqlZonenplanVersionMapping(filterVersionIds);

        return this.sqlReader.read(mapping);
    }
}
