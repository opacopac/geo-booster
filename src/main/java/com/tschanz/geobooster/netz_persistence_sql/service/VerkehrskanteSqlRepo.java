package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Verkehrskante;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegung;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz.service.VerkehrskanteAuspraegungPersistenceRepo;
import com.tschanz.geobooster.netz.service.VerkehrskantePersistenceRepo;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerkehrskanteElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerkehrskanteVersionConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class VerkehrskanteSqlRepo implements VerkehrskantePersistenceRepo {
    private final SqlConnectionFactory connectionFactory;
    private final VerkehrskanteAuspraegungPersistenceRepo vkaPersistenceRepo;


    @Override
    @SneakyThrows
    public Collection<Verkehrskante> readAllElements() {
        var sqlReader = new SqlReader<>(
            this.connectionFactory,
            new SqlVerkehrskanteElementConverter(),
            "%d vk elements loaded...",
            2
        );
        var query = String.format(
            "SELECT %s FROM N_VERKEHRSKANTE_E",
            String.join(",", SqlVerkehrskanteElementConverter.ALL_COLS)
        );

        return sqlReader.read(query);
    }


    @Override
    @SneakyThrows
    public Collection<VerkehrskanteVersion> readAllVersions() {
        // read vkas
        var vkaEs = this.vkaPersistenceRepo.readAllElements();
        //var vkaVList = this.vkaPersistenceRepo.readAllVersions(); // TODO: version map not needed?
        var vkasByEIdMap = this.createVkasByVkEIdMap(vkaEs);

        var sqlReader = new SqlReader<>(
            this.connectionFactory,
            new SqlVerkehrskanteVersionConverter(vkasByEIdMap),
            "%d vk versions loaded...",
            2
        );
        var query = String.format(
            "SELECT %s FROM N_VERKEHRSKANTE_V",
            String.join(",", SqlVerkehrskanteVersionConverter.ALL_COLS)
        );

        return sqlReader.read(query);
    }


    private Map<Long, List<VerkehrskanteAuspraegung>> createVkasByVkEIdMap(Collection<VerkehrskanteAuspraegung> vkaList) {
        Map<Long, List<VerkehrskanteAuspraegung>> vkasByVkEidMap = new HashMap<>();

        vkaList.forEach(vka -> {
            var vkEId = vka.getVerkehrskanteId();
            var vkVkas = vkasByVkEidMap.get(vkEId);
            if (vkVkas == null) {
                vkVkas = new ArrayList<>();
                vkVkas.add(vka);
                vkasByVkEidMap.put(vkEId, vkVkas);
            } else {
                vkVkas.add(vka);
            }
        });

        return vkasByVkEidMap;
    }
}
