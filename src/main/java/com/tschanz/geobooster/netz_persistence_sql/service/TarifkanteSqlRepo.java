package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Tarifkante;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz.service.TarifkantePersistenceRepo;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlTarifkanteElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlTarifkanteVersionConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlConnectionFactory;
import com.tschanz.geobooster.util.model.Timer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TarifkanteSqlRepo implements TarifkantePersistenceRepo {
    private static final Logger logger = LogManager.getLogger(TarifkanteSqlRepo.class);
    private static final int MAX_COUNT_TKV_CHANGED_SINCE = 1000; // limit for IN clause

    private final SqlConnectionFactory connectionFactory;


    @Override
    public Collection<Tarifkante> readAllElements() {
        return this.readAllElements(null);
    }


    @SneakyThrows
    public Collection<Tarifkante> readAllElements(LocalDate changedSince) {
        var sqlReader = new SqlReader<>(
            this.connectionFactory,
            new SqlTarifkanteElementConverter(),
            "%d tk elements loaded...",
            2
        );

        var query = String.format(
            "SELECT %s FROM N_TARIFKANTE_E",
            String.join(",", SqlTarifkanteElementConverter.SELECT_COLS)
        );

        if (changedSince != null) {
            query += this.getWhereClause(changedSince);
        }

        return sqlReader.read(query);
    }


    @Override
    public Collection<TarifkanteVersion> readAllVersions() {
        return this.readAllVersions(null);
    }


    @SneakyThrows
    public Collection<TarifkanteVersion> readAllVersions(LocalDate changedSince) {
        var sqlReader = new SqlReader<>(
            this.connectionFactory,
            new SqlTarifkanteVersionConverter(),
            "%d tk versions loaded...",
            2
        );

        var query = String.format(
            "SELECT %s FROM N_TARIFKANTE_V",
            String.join(",", SqlTarifkanteVersionConverter.SELECT_COLS)
        );

        if (changedSince != null) {
            query += this.getWhereClause(changedSince);
        }

        var tkVs = sqlReader.read(query);

        // add linked vks
        List<Long> onlyTkVIds = null;
        if (changedSince != null && tkVs.size() < MAX_COUNT_TKV_CHANGED_SINCE) {
            onlyTkVIds = tkVs.stream().map(TarifkanteVersion::getId).collect(Collectors.toList());
        }
        var tkVkMap = this.readTkVkMap(onlyTkVIds);
        tkVs.forEach(tkV -> {
            var vkIds = tkVkMap.get(tkV.getId());
            if (vkIds != null) {
                tkV.setVerkehrskanteIds(vkIds);
            }
        });

        return tkVs;
    }


    @SneakyThrows
    private Map<Long, List<Long>> readTkVkMap(List<Long> onlyTkVIds) {
        var connection = this.connectionFactory.createConnection();
        var query = "SELECT ID_TARIFKANTE_V, ID_VERKEHRS_KANTE_E FROM N_TARIFKANTE_X_N_VERK_KANTE_E";
        if (onlyTkVIds != null) {
            query += String.format(" WHERE ID_TARIFKANTE_V IN (%s)", onlyTkVIds.stream().map(Object::toString).collect(Collectors.joining(",")));
        }

        var tkVkMap = new HashMap<Long, List<Long>>();
        if (connection.getStatement().execute(query)) {
            var i = 0;
            var timer = new Timer();
            while (connection.getStatement().getResultSet().next()) {
                i++;
                if (timer.checkSecElapsed(2)) {
                    logger.info(i + " tk-vk mappings loaded...");
                }

                var resultSet = connection.getStatement().getResultSet();
                var tkVId = resultSet.getLong("ID_TARIFKANTE_V");
                var vkEId = resultSet.getLong("ID_VERKEHRS_KANTE_E");

                var tkVkMapEntry = tkVkMap.get(tkVId);
                if (tkVkMapEntry != null) {
                    tkVkMapEntry.add(vkEId);
                } else {
                    tkVkMap.put(tkVId, new ArrayList<>(Collections.singletonList(vkEId)));
                }
            }
        }

        connection.closeAll();

        return tkVkMap;
    }


    private String getWhereClause(LocalDate changedSince) {
        return String.format(
            " WHERE %s >= TO_DATE(%s, 'YYYY-MM-DD') OR %s >= TO_DATE(%s, 'YYYY-MM-DD')",
            SqlTarifkanteElementConverter.COL_CREATED_AT,
            changedSince.format(DateTimeFormatter.ISO_DATE),
            SqlTarifkanteElementConverter.COL_MODIFIED_AT,
            changedSince.format(DateTimeFormatter.ISO_DATE)
        );
    }
}
