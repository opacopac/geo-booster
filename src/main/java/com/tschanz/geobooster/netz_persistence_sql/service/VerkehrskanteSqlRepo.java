package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.*;
import com.tschanz.geobooster.netz_persistence.service.VerkehrskanteAuspraegungPersistenceRepo;
import com.tschanz.geobooster.netz_persistence.service.VerkehrskantePersistenceRepo;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerkehrskanteElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerkehrskanteVersionConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlConnectionFactory;
import com.tschanz.geobooster.util.model.Timer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class VerkehrskanteSqlRepo implements VerkehrskantePersistenceRepo {
    private static final Logger logger = LogManager.getLogger(VerkehrskanteSqlRepo.class);

    private final SqlConnectionFactory connectionFactory;
    private final VerkehrskanteAuspraegungPersistenceRepo vkaPersistenceRepo;


    @Override
    @SneakyThrows
    public Map<Long, Verkehrskante> readAllElements(Map<Long, Haltestelle> haltestelleMap) {
        var connection = this.connectionFactory.createConnection();
        var query = String.format(
            "SELECT %s FROM N_VERKEHRSKANTE_E",
            String.join(",", SqlVerkehrskanteElementConverter.ALL_COLS)
        );

        var elementMap = new HashMap<Long, Verkehrskante>();
        if (connection.getStatement().execute(query)) {
            var i = 0;
            var timer = new Timer();
            while (connection.getStatement().getResultSet().next()) {
                i++;
                if (timer.checkSecElapsed(2)) {
                    logger.info(i + " vk elements loaded...");
                }

                var resultSet = connection.getStatement().getResultSet();
                var element = SqlVerkehrskanteElementConverter.fromResultSet(resultSet, haltestelleMap);
                elementMap.put(element.getElementInfo().getId(), element);
            }
        }

        connection.closeAll();

        return elementMap;
    }


    @Override
    @SneakyThrows
    public Map<Long, VerkehrskanteVersion> readAllVersions(
        Map<Long, Verkehrskante> elementMap,
        Map<Long, Verwaltung> verwaltungMap
    ) {
        var connection = this.connectionFactory.createConnection();

        // read all vkas
        var vkaEMap = this.vkaPersistenceRepo.readAllElements(elementMap, verwaltungMap);
        var vkaVMap = this.vkaPersistenceRepo.readAllVersions(vkaEMap);
        var vkasByEIdMap = this.createVkasByVkEIdMap(elementMap, vkaEMap);

        var query = String.format(
            "SELECT %s FROM N_VERKEHRSKANTE_V",
            String.join(",", SqlVerkehrskanteVersionConverter.ALL_COLS)
        );

        var versionMap = new HashMap<Long, VerkehrskanteVersion>();
        if (connection.getStatement().execute(query)) {
            var i = 0;
            var timer = new Timer();
            while (connection.getStatement().getResultSet().next()) {
                i++;
                if (timer.checkSecElapsed(2)) {
                    logger.info(i + " vk versions loaded...");
                }

                var resultSet = connection.getStatement().getResultSet();
                var version = SqlVerkehrskanteVersionConverter.fromResultSet(resultSet, elementMap, vkasByEIdMap, verwaltungMap);
                versionMap.put(version.getVersionInfo().getId(), version);
            }
        }

        connection.closeAll();

        return versionMap;
    }


    private Map<Long, List<VerkehrskanteAuspraegung>> createVkasByVkEIdMap(
        Map<Long, Verkehrskante> vkMap,
        Map<Long, VerkehrskanteAuspraegung> vkaMap
    ) {
        Map<Long, List<VerkehrskanteAuspraegung>> vkasByVkEidMap = new HashMap<>();

        vkaMap.values().forEach(vka -> {
            var vkEId = vka.getVerkehrskante().getElementInfo().getId();
            var vkas = vkasByVkEidMap.get(vkEId);
            if (vkas == null) {
                vkas = new ArrayList<>();
                vkas.add(vka);
                vkasByVkEidMap.put(vkEId, vkas);
            } else {
                vkas.add(vka);
            }
        });

        return vkasByVkEidMap;
    }
}
