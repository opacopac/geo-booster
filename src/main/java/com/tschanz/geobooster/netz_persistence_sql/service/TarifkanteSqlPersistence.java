package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Tarifkante;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz_persistence.service.TarifkantePersistence;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlTarifkanteElementMapping;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlTarifkanteVersionMapping;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlTkVkMapping;
import com.tschanz.geobooster.persistence_sql.service.SqlStandardReader;
import com.tschanz.geobooster.util.model.KeyValue;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence.model.ElementVersionChanges;
import com.tschanz.geobooster.versioning_persistence_sql.service.SqlChangeDetector;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class TarifkanteSqlPersistence implements TarifkantePersistence {
    private final SqlStandardReader sqlStandardReader;
    private final SqlChangeDetector changeDetector;


    @Override
    public Collection<Tarifkante> readAllElements() {
        return this.readElements(Collections.emptyList());
    }


    @Override
    public Collection<TarifkanteVersion> readAllVersions() {
        return this.readVersions(Collections.emptyList());
    }


    @Override
    public ElementVersionChanges<Tarifkante, TarifkanteVersion> findChanges(LocalDateTime changedSince, Collection<Long> currentVersionIds) {
        var modifiedDeletedVersionIds = this.changeDetector.findModifiedDeletedIds(SqlTarifkanteVersionMapping.TABLE_NAME, changedSince, currentVersionIds);
        var modifiedVersionIds = modifiedDeletedVersionIds.getList1();
        Collection<TarifkanteVersion> modifiedVersions = !modifiedVersionIds.isEmpty() ? this.readVersions(modifiedVersionIds) : Collections.emptyList();
        var modifiedElementIds = modifiedVersions.stream().map(TarifkanteVersion::getElementId).distinct().collect(Collectors.toList());
        Collection<Tarifkante> modifiedElements = !modifiedElementIds.isEmpty() ? this.readElements(modifiedElementIds) : Collections.emptyList();

        return new ElementVersionChanges<>(
            modifiedElements,
            modifiedVersions,
            Collections.emptyList(), // ignoring deleted elements
            modifiedDeletedVersionIds.getList2()
        );
    }


    @SneakyThrows
    private Collection<Tarifkante> readElements(Collection<Long> elementIds) {
        var mapping = new SqlTarifkanteElementMapping(elementIds);

        return this.sqlStandardReader.read(mapping);
    }


    @SneakyThrows
    private Collection<TarifkanteVersion> readVersions(Collection<Long> versionIds) {
        var mapping = new SqlTarifkanteVersionMapping(versionIds);
        var tkVs = this.sqlStandardReader.read(mapping);

        if (tkVs.isEmpty()) {
            return tkVs; // no need to load tkvks
        }

        // add linked vks
        List<Long> filterTkVIds = !versionIds.isEmpty()
            ? tkVs.stream().map(TarifkanteVersion::getId).collect(Collectors.toList())
            : Collections.emptyList();
        var tkVkMap = this.readTkVkMap(filterTkVIds);
        tkVs.forEach(tkV -> {
            var vkIds = tkVkMap.get(tkV.getId());
            if (vkIds != null) {
                tkV.setVerkehrskanteIds(vkIds);
            }
        });

        return tkVs;
    }


    @SneakyThrows
    private Map<Long, Collection<Long>> readTkVkMap(List<Long> filterTkVIds) {
        var mapping = new SqlTkVkMapping(filterTkVIds);
        var tkVkList = this.sqlStandardReader.read(mapping);

        return ArrayHelper.create1toNLookupMap(tkVkList, KeyValue::getKey, KeyValue::getValue);
    }
}
