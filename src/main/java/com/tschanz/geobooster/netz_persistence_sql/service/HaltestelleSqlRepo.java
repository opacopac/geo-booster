package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz_persistence.service.HaltestellenPersistenceRepo;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlHaltestelleElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlHaltestelleVersionConverter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;


@Service
public class HaltestelleSqlRepo implements HaltestellenPersistenceRepo {
    @Override
    @SneakyThrows
    public Map<Long, Haltestelle> readAllElements() {
        var statement = this.getStatement();
        var query = String.format(
            "SELECT %s FROM N_HALTESTELLE_E",
            String.join(",", SqlHaltestelleElementConverter.ALL_COLS)
        );

        var elementMap = new HashMap<Long, Haltestelle>();
        if (statement.execute(query)) {
            while (statement.getResultSet().next()) {
                var element = SqlHaltestelleElementConverter.fromResultSet(statement.getResultSet());
                elementMap.put(element.getElementInfo().getId(), element);
            }
            statement.close();
        }

        return elementMap;
    }


    @Override
    @SneakyThrows
    public Map<Long, HaltestelleVersion> readAllVersions(Map<Long, Haltestelle> elementMap) {
        var statement = this.getStatement();
        var query = String.format(
            "SELECT %s FROM N_HALTESTELLE_V",
            String.join(",", SqlHaltestelleVersionConverter.ALL_COLS)
        );

        var versionMap = new HashMap<Long, HaltestelleVersion>();
        if (statement.execute(query)) {
            while (statement.getResultSet().next()) {
                var version = SqlHaltestelleVersionConverter.fromResultSet(statement.getResultSet(), elementMap);
                versionMap.put(version.getVersionInfo().getId(), version);
            }
            statement.close();
        }

        return versionMap;
    }


    @SneakyThrows
    // TODO
    private Statement getStatement() {
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/test",
            "geobooster",
            "geobooster"
        ).createStatement();
    }
}
