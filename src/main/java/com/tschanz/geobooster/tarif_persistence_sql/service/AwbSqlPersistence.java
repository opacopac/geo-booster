package com.tschanz.geobooster.tarif_persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.service.SqlStandardReader;
import com.tschanz.geobooster.tarif.model.Awb;
import com.tschanz.geobooster.tarif.model.AwbIncVerwaltung;
import com.tschanz.geobooster.tarif.model.AwbVersion;
import com.tschanz.geobooster.tarif_persistence.service.AwbPersistence;
import com.tschanz.geobooster.tarif_persistence_sql.model.*;
import com.tschanz.geobooster.util.model.KeyValue;
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
    private final SqlStandardReader sqlStandardReader;
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
    public KeyValue<Collection<AwbIncVerwaltung>, Collection<Long>> findAwbIncVerwaltungChanges(LocalDateTime changedSince, Collection<Long> currentIds) {
        var changes = this.changeDetector.findModifiedDeletedChanges(SqlAwbIncVerwaltungMapping.TABLE_NAME, changedSince, currentIds);
        var modifiedIds = changes.getModifiedIds();
        var deletedIds = changes.getDeletedIds();

        var modifiedAwbIncVerwaltungen = !modifiedIds.isEmpty() ? this.readAwbIncVerwaltungen(modifiedIds) : Collections.<AwbIncVerwaltung>emptyList();

        return new KeyValue<>(
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

        return this.sqlStandardReader.read(mapping);
    }


    private Collection<AwbVersion> readVersions(Collection<Long> versionIds) {
        var includeVkMap = this.readIncludeVkMap(versionIds);
        var excludeVkMap = this.readExcludeVkMap(versionIds);
        var includeTkMap = this.readIncludeTkMap(versionIds);
        var excludeTkMap = this.readExcludeTkMap(versionIds);
        var includeVerwMap = this.readIncludeVerwMap(versionIds);
        var includeZpMap = this.readIncludeZpMap(versionIds);
        var includeRgaMap = this.readIncludeRgaMap(versionIds);
        var mapping = new SqlAwbVersionMapping(
            versionIds,
            includeVkMap,
            excludeVkMap,
            includeTkMap,
            excludeTkMap,
            includeVerwMap,
            includeZpMap,
            includeRgaMap
        );

        return this.sqlStandardReader.read(mapping);
    }


    private Collection<AwbIncVerwaltung> readAwbIncVerwaltungen(Collection<Long> versionIds) {
        var mapping = new SqlAwbIncVerwaltungMapping(versionIds);

        return this.sqlStandardReader.read(mapping);
    }


    private Map<Long, Collection<Long>> readIncludeVkMap(Collection<Long> awbVersionIds) {
        var mapping = new SqlAwbIncVkMapping(awbVersionIds);
        var excludeVks = this.sqlStandardReader.read(mapping);

        return ArrayHelper.create1toNLookupMap(excludeVks, KeyValue::getKey, KeyValue::getValue);
    }


    private Map<Long, Collection<Long>> readExcludeVkMap(Collection<Long> awbVersionIds) {
        var mapping = new SqlAwbExcVkMapping(awbVersionIds);
        var excludeVks = this.sqlStandardReader.read(mapping);

        return ArrayHelper.create1toNLookupMap(excludeVks, KeyValue::getKey, KeyValue::getValue);
    }


    private Map<Long, Collection<Long>> readIncludeTkMap(Collection<Long> awbVersionIds) {
        var mapping = new SqlAwbIncTkMapping(awbVersionIds);
        var excludeTks = this.sqlStandardReader.read(mapping);

        return ArrayHelper.create1toNLookupMap(excludeTks, KeyValue::getKey, KeyValue::getValue);
    }


    private Map<Long, Collection<Long>> readExcludeTkMap(Collection<Long> awbVersionIds) {
        var mapping = new SqlAwbExcTkMapping(awbVersionIds);
        var excludeTks = this.sqlStandardReader.read(mapping);

        return ArrayHelper.create1toNLookupMap(excludeTks, KeyValue::getKey, KeyValue::getValue);
    }


    private Map<Long, Collection<Long>> readIncludeVerwMap(Collection<Long> awbVersionIds) {
        var mapping = new SqlAwbIncVerwMapping(awbVersionIds);
        var excludeVks = this.sqlStandardReader.read(mapping);

        return ArrayHelper.create1toNLookupMap(excludeVks, KeyValue::getKey, KeyValue::getValue);
    }


    private Map<Long, Collection<Long>> readIncludeZpMap(Collection<Long> awbVersionIds) {
        var mapping = new SqlAwbIncZpMapping(awbVersionIds);
        var excludeVks = this.sqlStandardReader.read(mapping);

        return ArrayHelper.create1toNLookupMap(excludeVks, KeyValue::getKey, KeyValue::getValue);
    }


    private Map<Long, Collection<Long>> readIncludeRgaMap(Collection<Long> awbVersionIds) {
        var mapping = new SqlAwbIncRgaMapping(awbVersionIds);
        var excludeVks = sqlStandardReader.read(mapping);

        return ArrayHelper.create1toNLookupMap(excludeVks, KeyValue::getKey, KeyValue::getValue);
    }
}
