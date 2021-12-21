package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.netz.model.Verwaltung;
import com.tschanz.geobooster.netz.model.VerwaltungVersion;
import com.tschanz.geobooster.netz_persistence.service.VerwaltungPersistenceRepo;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerwaltungElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerwaltungVersionConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class VerwaltungSqlRepo implements VerwaltungPersistenceRepo {
    private final SqlConnectionFactory connectionFactory;


    @Override
    @SneakyThrows
    public Map<Long, Verwaltung> readAllElements(Map<Long, Betreiber> betreiberMap) {
        var connection = this.connectionFactory.createConnection();
        var query = String.format(
            "SELECT %s FROM N_VERWALTUNG_E",
            String.join(",", SqlVerwaltungElementConverter.ALL_COLS)
        );

        var elementMap = new HashMap<Long, Verwaltung>();
        if (connection.getStatement().execute(query)) {
            while (connection.getStatement().getResultSet().next()) {
                var resultSet = connection.getStatement().getResultSet();
                var element = SqlVerwaltungElementConverter.fromResultSet(resultSet, betreiberMap);
                elementMap.put(element.getElementInfo().getId(), element);
            }
        }

        connection.closeAll();

        return elementMap;
    }


    @Override
    @SneakyThrows
    public Map<Long, VerwaltungVersion> readAllVersions(Map<Long, Verwaltung> elementMap) {
        var connection = this.connectionFactory.createConnection();
        var query = String.format(
            "SELECT %s FROM N_VERWALTUNG_V",
            String.join(",", SqlVerwaltungVersionConverter.ALL_COLS)
        );

        var versionMap = new HashMap<Long, VerwaltungVersion>();
        if (connection.getStatement().execute(query)) {
            while (connection.getStatement().getResultSet().next()) {
                var resultSet = connection.getStatement().getResultSet();
                var version = SqlVerwaltungVersionConverter.fromResultSet(resultSet, elementMap);
                versionMap.put(version.getVersionInfo().getId(), version);
            }
        }

        connection.closeAll();

        return versionMap;
    }
}
