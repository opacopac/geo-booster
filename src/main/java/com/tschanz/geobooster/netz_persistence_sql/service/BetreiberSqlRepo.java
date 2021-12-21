package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.netz.model.BetreiberVersion;
import com.tschanz.geobooster.netz_persistence.service.BetreiberPersistenceRepo;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlBetreiberElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlBetreiberVersionConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class BetreiberSqlRepo implements BetreiberPersistenceRepo {
    private final SqlConnectionFactory connectionFactory;


    @Override
    @SneakyThrows
    public Map<Long, Betreiber> readAllElements() {
        var connection = this.connectionFactory.createConnection();
        var query = String.format(
            "SELECT %s FROM N_BETREIBER_E",
            String.join(",", SqlBetreiberElementConverter.ALL_COLS)
        );

        var elementMap = new HashMap<Long, Betreiber>();
        if (connection.getStatement().execute(query)) {
            while (connection.getStatement().getResultSet().next()) {
                var resultSet = connection.getStatement().getResultSet();
                var element = SqlBetreiberElementConverter.fromResultSet(resultSet);
                elementMap.put(element.getElementInfo().getId(), element);
            }
        }

        connection.closeAll();

        return elementMap;
    }


    @Override
    @SneakyThrows
    public Map<Long, BetreiberVersion> readAllVersions(Map<Long, Betreiber> elementMap) {
        var connection = this.connectionFactory.createConnection();
        var query = String.format(
            "SELECT %s FROM N_BETREIBER_V",
            String.join(",", SqlBetreiberVersionConverter.ALL_COLS)
        );

        var versionMap = new HashMap<Long, BetreiberVersion>();
        if (connection.getStatement().execute(query)) {
            while (connection.getStatement().getResultSet().next()) {
                var resultSet = connection.getStatement().getResultSet();
                var version = SqlBetreiberVersionConverter.fromResultSet(resultSet, elementMap);
                versionMap.put(version.getVersionInfo().getId(), version);
            }
        }

        connection.closeAll();

        return versionMap;
    }
}
