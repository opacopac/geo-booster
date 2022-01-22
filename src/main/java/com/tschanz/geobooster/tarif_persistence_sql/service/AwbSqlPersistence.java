package com.tschanz.geobooster.tarif_persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.service.SqlStandardReader;
import com.tschanz.geobooster.tarif.model.Awb;
import com.tschanz.geobooster.tarif.model.AwbIncVerwaltung;
import com.tschanz.geobooster.tarif.model.AwbVersion;
import com.tschanz.geobooster.tarif_persistence.service.AwbPersistence;
import com.tschanz.geobooster.tarif_persistence_sql.model.*;
import com.tschanz.geobooster.util.model.Tuple2;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence.model.ElementVersionChanges;
import com.tschanz.geobooster.versioning_persistence_sql.service.SqlChangeDetector;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AwbSqlPersistence implements AwbPersistence {
    private final SqlStandardReader sqlReader;
    private final SqlChangeDetector changeDetector;


    @Override
    public ElementVersionChanges<Awb, AwbVersion> findAwbVersionChanges(LocalDateTime changedSince, Collection<Long> currentIds) {
        var changes = this.changeDetector.findModifiedDeletedChanges(SqlAwbVersionMapping.TABLE_NAME, changedSince, currentIds);
        var modifiedVersionIds = changes.getModifiedIds();
        var deletedVersionIds = changes.getDeletedIds();

        var modifiedVersions = !modifiedVersionIds.isEmpty() ? this.readVersions(modifiedVersionIds) : Collections.<AwbVersion>emptyList();
        var modifiedElementIds = modifiedVersions.stream().map(AwbVersion::getElementId).distinct().collect(Collectors.toList());
        var modifiedElements = !modifiedElementIds.isEmpty() ? this.readElements(modifiedElementIds) : Collections.<Awb>emptyList();

        return new ElementVersionChanges<>(
            modifiedElements,
            modifiedVersions,
            Collections.emptyList(), // ignoring deleted elements
            deletedVersionIds
        );
    }


    @Override
    public Tuple2<Collection<AwbIncVerwaltung>, Collection<Long>> findAwbIncVerwaltungChanges(LocalDateTime changedSince, Collection<Long> currentIds) {
        var changes = this.changeDetector.findModifiedDeletedChanges(SqlAwbIncVerwaltungMapping.TABLE_NAME, changedSince, currentIds);
        var modifiedIds = changes.getModifiedIds();
        var deletedIds = changes.getDeletedIds();

        var modifiedAwbIncVerwaltungen = !modifiedIds.isEmpty() ? this.readAwbIncVerwaltungen(modifiedIds) : Collections.<AwbIncVerwaltung>emptyList();

        return new Tuple2<>(
            modifiedAwbIncVerwaltungen,
            deletedIds
        );
    }


    @Override
    @SneakyThrows
    public Collection<Awb> readAllElements() {
        return this.readElements(Collections.emptyList());
    }


    @Override
    @SneakyThrows
    public Collection<AwbVersion> readAllVersions() {
        return this.readVersions(Collections.emptyList());
    }


    @Override
    public Collection<AwbIncVerwaltung> readAllAwbIncVerwaltungen() {
        return this.readAwbIncVerwaltungen(Collections.emptyList());
    }


    private Collection<Awb> readElements(Collection<Long> elementIds) {
        var mapping = new SqlAwbElementMapping(elementIds);

        return this.sqlReader.read(mapping);
    }


    private Collection<AwbVersion> readVersions(Collection<Long> versionIds) {
        var includeVkMap = this.readIncludeVkMap(versionIds);
        var excludeVkMap = this.readExcludeVkMap(versionIds);
        var includeTkMap = this.readIncludeTkMap(versionIds);
        var excludeTkMap = this.readExcludeTkMap(versionIds);
        var includeZpMap = this.readIncludeZpMap(versionIds);
        var includeRgaMap = this.readIncludeRgaMap(versionIds);
        var mapping = new SqlAwbVersionMapping(
            versionIds,
            includeVkMap,
            excludeVkMap,
            includeTkMap,
            excludeTkMap,
            includeZpMap,
            includeRgaMap
        );

        return this.sqlReader.read(mapping);
    }


    private Collection<AwbIncVerwaltung> readAwbIncVerwaltungen(Collection<Long> ids) {
        var mapping = new SqlAwbIncVerwaltungMapping(ids);

        return this.sqlReader.read(mapping);
    }


    private Map<Long, Collection<Long>> readIncludeVkMap(Collection<Long> awbVersionIds) {
        var mapping = new SqlAwbIncVkMapping(awbVersionIds);
        var excludeVks = this.sqlReader.read(mapping);

        return ArrayHelper.create1toNLookupMap(excludeVks, Tuple2::getFirst, Tuple2::getSecond);
    }


    private Map<Long, Collection<Long>> readExcludeVkMap(Collection<Long> awbVersionIds) {
        var mapping = new SqlAwbExcVkMapping(awbVersionIds);
        var excludeVks = this.sqlReader.read(mapping);

        return ArrayHelper.create1toNLookupMap(excludeVks, Tuple2::getFirst, Tuple2::getSecond);
    }


    private Map<Long, Collection<Long>> readIncludeTkMap(Collection<Long> awbVersionIds) {
        var mapping = new SqlAwbIncTkMapping(awbVersionIds);
        var excludeTks = this.sqlReader.read(mapping);

        return ArrayHelper.create1toNLookupMap(excludeTks, Tuple2::getFirst, Tuple2::getSecond);
    }


    private Map<Long, Collection<Long>> readExcludeTkMap(Collection<Long> awbVersionIds) {
        var mapping = new SqlAwbExcTkMapping(awbVersionIds);
        var excludeTks = this.sqlReader.read(mapping);

        return ArrayHelper.create1toNLookupMap(excludeTks, Tuple2::getFirst, Tuple2::getSecond);
    }


    private Map<Long, Collection<Long>> readIncludeZpMap(Collection<Long> awbVersionIds) {
        var mapping = new SqlAwbIncZpMapping(awbVersionIds);
        var excludeVks = this.sqlReader.read(mapping);

        return ArrayHelper.create1toNLookupMap(excludeVks, Tuple2::getFirst, Tuple2::getSecond);
    }


    private Map<Long, Collection<Long>> readIncludeRgaMap(Collection<Long> awbVersionIds) {
        var mapping = new SqlAwbIncRgaMapping(awbVersionIds);
        var excludeVks = sqlReader.read(mapping);

        return ArrayHelper.create1toNLookupMap(excludeVks, Tuple2::getFirst, Tuple2::getSecond);
    }
}
