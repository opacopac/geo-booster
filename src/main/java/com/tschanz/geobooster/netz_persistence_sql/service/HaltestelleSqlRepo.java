package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz_persistence.service.HaltestellenPersistenceRepo;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlHaltestelleElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlHaltestelleVersionConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class HaltestelleSqlRepo implements HaltestellenPersistenceRepo {
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
            while (connection.getStatement().getResultSet().next()) {
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
            while (connection.getStatement().getResultSet().next()) {
                var resultSet = connection.getStatement().getResultSet();
                var version = SqlHaltestelleVersionConverter.fromResultSet(resultSet, elementMap);
                versionMap.put(version.getVersionInfo().getId(), version);
            }
        }

        connection.closeAll();

        return versionMap;
    }
}
