package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz_persistence.service.HaltestellenPersistenceRepo;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlHaltestelleElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlHaltestelleVersionConverter;
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
public class HaltestelleSqlRepo implements HaltestellenPersistenceRepo {
    private static final Logger logger = LogManager.getLogger(HaltestelleSqlRepo.class);

    private final SqlConnectionFactory connectionFactory;


    @Override
    @SneakyThrows
    public Map<Long, Haltestelle> readAllElements() {
        var connection = this.connectionFactory.createConnection();
        var query = String.format(
            "SELECT %s FROM N_HALTESTELLE_E",
            String.join(",", SqlHaltestelleElementConverter.ALL_COLS)
        );

        var elementMap = new HashMap<Long, Haltestelle>();
        if (connection.getStatement().execute(query)) {
            var i = 0;
            var timer = new Timer();
            while (connection.getStatement().getResultSet().next()) {
                i++;
                if (timer.checkSecElapsed(2)) {
                    logger.info(i + " hst elements loaded...");
                }

                var resultSet = connection.getStatement().getResultSet();
                var element = SqlHaltestelleElementConverter.fromResultSet(resultSet);
                elementMap.put(element.getElementInfo().getId(), element);
            }
        }

        connection.closeAll();

        return elementMap;
    }


    @Override
    @SneakyThrows
    public Map<Long, HaltestelleVersion> readAllVersions(Map<Long, Haltestelle> elementMap) {
        var connection = this.connectionFactory.createConnection();
        var query = String.format(
            "SELECT %s FROM N_HALTESTELLE_V",
            String.join(",", SqlHaltestelleVersionConverter.ALL_COLS)
        );

        var versionMap = new HashMap<Long, HaltestelleVersion>();
        if (connection.getStatement().execute(query)) {
            var i = 0;
            var timer = new Timer();
            while (connection.getStatement().getResultSet().next()) {
                i++;
                if (timer.checkSecElapsed(2)) {
                    logger.info(i + " hst versions loaded...");
                }

                var resultSet = connection.getStatement().getResultSet();
                var version = SqlHaltestelleVersionConverter.fromResultSet(resultSet, elementMap);
                versionMap.put(version.getVersionInfo().getId(), version);
            }
        }

        connection.closeAll();

        return versionMap;
    }
}
