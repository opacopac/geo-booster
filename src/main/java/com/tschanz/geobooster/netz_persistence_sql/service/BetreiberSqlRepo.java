package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.netz.model.BetreiberVersion;
import com.tschanz.geobooster.netz_persistence.service.BetreiberPersistenceRepo;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlBetreiberElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlBetreiberVersionConverter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;


@Service
public class BetreiberSqlRepo implements BetreiberPersistenceRepo {
    @Override
    @SneakyThrows
    public Map<Long, Betreiber> readAllElements() {
        var statement = this.getStatement();
        var query = String.format(
            "SELECT %s FROM N_BETREIBER_E",
            String.join(",", SqlBetreiberElementConverter.ALL_COLS)
        );

        var elementMap = new HashMap<Long, Betreiber>();
        if (statement.execute(query)) {
            while (statement.getResultSet().next()) {
                var element = SqlBetreiberElementConverter.fromResultSet(statement.getResultSet());
                elementMap.put(element.getElementInfo().getId(), element);
            }
            statement.close();
        }

        return elementMap;
    }


    @Override
    @SneakyThrows
    public Map<Long, BetreiberVersion> readAllVersions(Map<Long, Betreiber> elementMap) {
        var statement = this.getStatement();
        var query = String.format(
            "SELECT %s FROM N_BETREIBER_V",
            String.join(",", SqlBetreiberVersionConverter.ALL_COLS)
        );

        var versionMap = new HashMap<Long, BetreiberVersion>();
        if (statement.execute(query)) {
            while (statement.getResultSet().next()) {
                var version = SqlBetreiberVersionConverter.fromResultSet(statement.getResultSet(), elementMap);
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
