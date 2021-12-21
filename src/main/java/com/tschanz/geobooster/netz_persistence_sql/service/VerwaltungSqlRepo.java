package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.netz.model.Verwaltung;
import com.tschanz.geobooster.netz.model.VerwaltungVersion;
import com.tschanz.geobooster.netz_persistence.service.VerwaltungPersistenceRepo;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerwaltungElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerwaltungVersionConverter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;


@Service
public class VerwaltungSqlRepo implements VerwaltungPersistenceRepo {
    @Override
    @SneakyThrows
    public Map<Long, Verwaltung> readAllElements(Map<Long, Betreiber> betreiberMap) {
        var statement = this.getStatement();
        var query = String.format(
            "SELECT %s FROM N_VERWALTUNG_E",
            String.join(",", SqlVerwaltungElementConverter.ALL_COLS)
        );

        var elementMap = new HashMap<Long, Verwaltung>();
        if (statement.execute(query)) {
            while (statement.getResultSet().next()) {
                var element = SqlVerwaltungElementConverter.fromResultSet(statement.getResultSet(), betreiberMap);
                elementMap.put(element.getElementInfo().getId(), element);
            }
            statement.close();
        }

        return elementMap;
    }


    @Override
    @SneakyThrows
    public Map<Long, VerwaltungVersion> readAllVersions(Map<Long, Verwaltung> elementMap) {
        var statement = this.getStatement();
        var query = String.format(
            "SELECT %s FROM N_VERWALTUNG_V",
            String.join(",", SqlVerwaltungVersionConverter.ALL_COLS)
        );

        var versionMap = new HashMap<Long, VerwaltungVersion>();
        if (statement.execute(query)) {
            while (statement.getResultSet().next()) {
                var version = SqlVerwaltungVersionConverter.fromResultSet(statement.getResultSet(), elementMap);
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
