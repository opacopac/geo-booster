package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Verkehrskante;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegung;
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
        var query = String.format(
            "SELECT %s FROM N_VERKEHRSKANTE_E",
            String.join(",", SqlVerkehrskanteElementConverter.SELECT_COLS)
        );

        return this.sqlReader.read(query, new SqlVerkehrskanteElementConverter());
    }


    @Override
    @SneakyThrows
    public Collection<VerkehrskanteVersion> readAllVersions() {
        var vkaList = this.vkaPersistenceRepo.readAllElements();
        //var vkaVList = this.vkaPersistenceRepo.readAllVersions(); // TODO: version map not needed?
        var vkVkasMap = this.createVkVkasMap(vkaList);

        var query = String.format(
            "SELECT %s FROM N_VERKEHRSKANTE_V",
            String.join(",", SqlVerkehrskanteVersionConverter.SELECT_COLS)
        );

        return this.sqlReader.read(query, new SqlVerkehrskanteVersionConverter(vkVkasMap));
    }


    private Map<Long, List<VerkehrskanteAuspraegung>> createVkVkasMap(Collection<VerkehrskanteAuspraegung> vkaList) {
        Map<Long, List<VerkehrskanteAuspraegung>> vkVkasMap = new HashMap<>();

        vkaList.forEach(vka -> {
            var vkId = vka.getVerkehrskanteId();
            var vkVkas = vkVkasMap.computeIfAbsent(vkId, k -> new ArrayList<>());
            vkVkas.add(vka);
        });

        return vkVkasMap;
    }
}
