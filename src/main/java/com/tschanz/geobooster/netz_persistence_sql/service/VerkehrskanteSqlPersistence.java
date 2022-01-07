package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Verkehrskante;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegung;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegungVersion;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz_persistence.service.VerkehrskanteAuspraegungPersistence;
import com.tschanz.geobooster.netz_persistence.service.VerkehrskantePersistence;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerkehrskanteElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerkehrskanteVersionConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlReader;
import com.tschanz.geobooster.versioning.service.VersioningHelper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Repository
@RequiredArgsConstructor
public class VerkehrskanteSqlPersistence implements VerkehrskantePersistence {
    private final SqlReader sqlReader;
    private final VerkehrskanteAuspraegungPersistence vkaPersistenceRepo;


    @Override
    @SneakyThrows
    public Collection<Verkehrskante> readAllElements() {
        return this.sqlReader.read(new SqlVerkehrskanteElementConverter());
    }


    @Override
    @SneakyThrows
    public Collection<VerkehrskanteVersion> readAllVersions() {
        var vkVkaMap = this.readVkVkaMap();
        var vkaVersionMap = this.readVkaVersionMap();
        var converter = new SqlVerkehrskanteVersionConverter(vkVkaMap, vkaVersionMap);

        return this.sqlReader.read(converter);
    }


    private Map<Long, Collection<VerkehrskanteAuspraegung>> readVkVkaMap() {
        var vkaEList = this.vkaPersistenceRepo.readAllElements();

        Map<Long, Collection<VerkehrskanteAuspraegung>> vkVkasMap = new HashMap<>();
        vkaEList.forEach(vka -> {
            var vkId = vka.getVerkehrskanteId();
            var vkVkas = vkVkasMap.computeIfAbsent(vkId, k -> new ArrayList<>());
            vkVkas.add(vka);
        });

        return vkVkasMap;
    }


    private Map<Long, Collection<VerkehrskanteAuspraegungVersion>> readVkaVersionMap() {
        var vkaVersionList = this.vkaPersistenceRepo.readAllVersions();

        return VersioningHelper.createElementIdMap(vkaVersionList);
    }
}