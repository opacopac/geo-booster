package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.netz.model.Verkehrskante;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz_persistence.service.VerkehrskantePersistenceRepo;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerkehrskanteElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerkehrskanteVersionConverter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;


@Service
public class VehrkehrskanteSqlRepo implements VerkehrskantePersistenceRepo {
    @Override
    @SneakyThrows
    public Map<Long, Verkehrskante> readAllElements(Map<Long, Haltestelle> haltestelleMap) {
        var statement = this.getStatement();
        var query = String.format(
            "SELECT %s FROM N_VERKEHRSKANTE_E",
            String.join(",", SqlVerkehrskanteElementConverter.ALL_COLS)
        );

        var elementMap = new HashMap<Long, Verkehrskante>();
        if (statement.execute(query)) {
            while (statement.getResultSet().next()) {
                var element = SqlVerkehrskanteElementConverter.fromResultSet(statement.getResultSet(), haltestelleMap);
                elementMap.put(element.getElementInfo().getId(), element);
            }
            statement.close();
        }

        return elementMap;
    }


    @Override
    @SneakyThrows
    public Map<Long, VerkehrskanteVersion> readAllVersions(Map<Long, Verkehrskante> elementMap) {
        var statement = this.getStatement();
        var query = String.format(
            "SELECT %s FROM N_VERKEHRSKANTE_V",
            String.join(",", SqlVerkehrskanteVersionConverter.ALL_COLS)
        );

        var versionMap = new HashMap<Long, VerkehrskanteVersion>();
        if (statement.execute(query)) {
            while (statement.getResultSet().next()) {
                var version = SqlVerkehrskanteVersionConverter.fromResultSet(statement.getResultSet(), elementMap);
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
