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
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.*;


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


    private Map<Long, List<VerkehrskanteAuspraegung>> readVkVkaMap() {
        var vkaEList = this.vkaPersistenceRepo.readAllElements();

        Map<Long, List<VerkehrskanteAuspraegung>> vkVkasMap = new HashMap<>();
        vkaEList.forEach(vka -> {
            var vkId = vka.getVerkehrskanteId();
            var vkVkas = vkVkasMap.computeIfAbsent(vkId, k -> new ArrayList<>());
            vkVkas.add(vka);
        });

        return vkVkasMap;
    }


    private Map<Long, List<VerkehrskanteAuspraegungVersion>> readVkaVersionMap() {
        var vkaVersionList = this.vkaPersistenceRepo.readAllVersions();

        Map<Long, List<VerkehrskanteAuspraegungVersion>> vkaVersionMap = new HashMap<>();
        vkaVersionList.forEach(vkaV -> {
            var vkaVersions = vkaVersionMap.computeIfAbsent(vkaV.getElementId(), k -> new ArrayList<>());
            vkaVersions.add(vkaV);
        });

        return vkaVersionMap;
    }
}