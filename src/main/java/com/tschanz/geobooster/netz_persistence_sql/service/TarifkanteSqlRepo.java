package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.netz.model.Tarifkante;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz_persistence.service.TarifkantePersistenceRepo;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlTarifkanteElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlTarifkanteVersionConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class TarifkanteSqlRepo implements TarifkantePersistenceRepo {
    private final SqlConnectionFactory connectionFactory;


    @Override
    @SneakyThrows
    public Map<Long, Tarifkante> readAllElements(Map<Long, Haltestelle> haltestelleMap) {
        var connection = this.connectionFactory.createConnection();
        var query = String.format(
            "SELECT %s FROM N_TARIFKANTE_E",
            String.join(",", SqlTarifkanteElementConverter.ALL_COLS)
        );

        var elementMap = new HashMap<Long, Tarifkante>();
        if (connection.getStatement().execute(query)) {
            while (connection.getStatement().getResultSet().next()) {
                var resultSet = connection.getStatement().getResultSet();
                var element = SqlTarifkanteElementConverter.fromResultSet(resultSet, haltestelleMap);
                elementMap.put(element.getElementInfo().getId(), element);
            }
        }

        connection.closeAll();

        return elementMap;
    }


    @Override
    @SneakyThrows
    public Map<Long, TarifkanteVersion> readAllVersions(Map<Long, Tarifkante> elementMap) {
        var connection = this.connectionFactory.createConnection();
        var query = String.format(
            "SELECT %s FROM N_TARIFKANTE_V",
            String.join(",", SqlTarifkanteVersionConverter.ALL_COLS)
        );

        var versionMap = new HashMap<Long, TarifkanteVersion>();
        if (connection.getStatement().execute(query)) {
            while (connection.getStatement().getResultSet().next()) {
                var resultSet = connection.getStatement().getResultSet();
                var version = SqlTarifkanteVersionConverter.fromResultSet(resultSet, elementMap);
                versionMap.put(version.getVersionInfo().getId(), version);
            }
        }

        connection.closeAll();

        return versionMap;
    }
}
