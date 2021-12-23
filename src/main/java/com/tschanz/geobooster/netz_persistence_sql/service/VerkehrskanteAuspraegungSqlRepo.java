package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Verkehrskante;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegung;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegungVersion;
import com.tschanz.geobooster.netz.model.Verwaltung;
import com.tschanz.geobooster.netz_persistence.service.VerkehrskanteAuspraegungPersistenceRepo;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerkehrskanteAuspraegungElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerkehrskanteAuspraegungVersionConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlConnectionFactory;
import com.tschanz.geobooster.util.model.Timer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class VerkehrskanteAuspraegungSqlRepo implements VerkehrskanteAuspraegungPersistenceRepo {
    private static final Logger logger = LogManager.getLogger(VerkehrskanteAuspraegungSqlRepo.class);

    private final SqlConnectionFactory connectionFactory;


    @Override
    @SneakyThrows
    public Map<Long, VerkehrskanteAuspraegung> readAllElements(Map<Long, Verkehrskante> verkehrskanteMap, Map<Long, Verwaltung> verwaltungMap) {
        var connection = this.connectionFactory.createConnection();
        var query = String.format(
            "SELECT %s FROM N_VERKEHRS_KANTE_AUSPR_E",
            String.join(",", SqlVerkehrskanteAuspraegungElementConverter.ALL_COLS)
        );

        var elementMap = new HashMap<Long, VerkehrskanteAuspraegung>();
        if (connection.getStatement().execute(query)) {
            var i = 0;
            var timer = new Timer();
            while (connection.getStatement().getResultSet().next()) {
                i++;
                if (timer.checkSecElapsed(2)) {
                    logger.info(i + " vka elements loaded...");
                }

                var resultSet = connection.getStatement().getResultSet();
                var element = SqlVerkehrskanteAuspraegungElementConverter.fromResultSet(resultSet, verkehrskanteMap, verwaltungMap);
                elementMap.put(element.getElementInfo().getId(), element);
            }
        }

        connection.closeAll();

        return elementMap;
    }


    @Override
    @SneakyThrows
    public Map<Long, VerkehrskanteAuspraegungVersion> readAllVersions(Map<Long, VerkehrskanteAuspraegung> elementMap) {
        var connection = this.connectionFactory.createConnection();
        var query = String.format(
            "SELECT %s FROM N_VERKEHRS_KANTE_AUSPR_V",
            String.join(",", SqlVerkehrskanteAuspraegungVersionConverter.ALL_COLS)
        );

        var versionMap = new HashMap<Long, VerkehrskanteAuspraegungVersion>();
        if (connection.getStatement().execute(query)) {
            var i = 0;
            var timer = new Timer();
            while (connection.getStatement().getResultSet().next()) {
                i++;
                if (timer.checkSecElapsed(2)) {
                    logger.info(i + " vka versions loaded...");
                }

                var resultSet = connection.getStatement().getResultSet();
                var version = SqlVerkehrskanteAuspraegungVersionConverter.fromResultSet(resultSet, elementMap);
                versionMap.put(version.getVersionInfo().getId(), version);
            }
        }

        connection.closeAll();

        return versionMap;
    }
}
