package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Tarifkante;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz_persistence.model.ReadFilter;
import com.tschanz.geobooster.netz_persistence.service.TarifkantePersistence;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlTarifkanteElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlTarifkanteVersionConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlConnectionFactory;
import com.tschanz.geobooster.persistence_sql.service.SqlHelper;
import com.tschanz.geobooster.persistence_sql.service.SqlReader;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class TarifkanteSqlPersistence implements TarifkantePersistence {
    private final SqlConnectionFactory connectionFactory;
    private final SqlReader sqlReader;


    @Override
    public Collection<Tarifkante> readAllElements() {
        return this.readElements(null);
    }


    @Override
    @SneakyThrows
    public Collection<Tarifkante> readElements(ReadFilter filter) {
        var query = String.format(
            "SELECT %s FROM N_TARIFKANTE_E",
            String.join(",", SqlTarifkanteElementConverter.SELECT_COLS)
        );
        if (filter != null) {
            query += this.getWhereClauseForFilter(filter);
        }

        return this.sqlReader.read(query, new SqlTarifkanteElementConverter());
    }


    @Override
    public Collection<TarifkanteVersion> readAllVersions() {
        return this.readVersions(null);
    }


    @Override
    @SneakyThrows
    public Collection<TarifkanteVersion> readVersions(ReadFilter filter) {
        var query = String.format(
            "SELECT %s FROM N_TARIFKANTE_V",
            String.join(",", SqlTarifkanteVersionConverter.SELECT_COLS)
        );
        if (filter != null) {
            query += this.getWhereClauseForFilter(filter);
        }
        var tkVs = this.sqlReader.read(query, new SqlTarifkanteVersionConverter());

        // add linked vks
        List<Long> onlyTkVIds = null;
        if (filter != null && tkVs.size() > 0) {
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


    @Override
    @SneakyThrows
    public long readVersionCount() {
        var connection = this.connectionFactory.getConnection();
        var query = "SELECT COUNT(*) AS TKV_COUNT FROM N_TARIFKANTE_V";

        var tkvCount = 0L;
        if (connection.getStatement().execute(query)) {
            connection.getStatement().getResultSet().next();
            var resultSet = connection.getStatement().getResultSet();
            tkvCount = resultSet.getLong("TKV_COUNT");
        }

        connection.closeResultsetAndStatement();

        return tkvCount;
    }


    @Override
    @SneakyThrows
    public Collection<Long> readAllVersionIds() {
        var query = String.format(
            "SELECT %s FROM N_TARIFKANTE_V",
            String.join(",", SqlHasIdConverter.SELECT_COLS)
        );

        return this.sqlReader.read(query, new SqlHasIdConverter());
    }


    @SneakyThrows
    private Map<Long, List<Long>> readTkVkMap(List<Long> onlyTkVIds) {
        var connection = this.connectionFactory.getConnection();
        var query = "SELECT ID_TARIFKANTE_V, ID_VERKEHRS_KANTE_E FROM N_TARIFKANTE_X_N_VERK_KANTE_E";
        if (onlyTkVIds != null) {
            query += String.format(" WHERE ID_TARIFKANTE_V IN (%s)", onlyTkVIds.stream().map(Object::toString).collect(Collectors.joining(",")));
        }

        var tkVkMap = new HashMap<Long, List<Long>>();
        if (connection.getStatement().execute(query)) {
            while (connection.getStatement().getResultSet().next()) {
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

        connection.closeResultsetAndStatement();

        return tkVkMap;
    }


    private String getWhereClauseForFilter(ReadFilter filter) {
        List<String> conditions = new ArrayList<>();

        if (filter.getIdList() != null) {
            var idStrings = filter.getIdList().stream().map(Object::toString).collect(Collectors.toList());
            conditions.add(String.format("(%s IN (%s))",
                SqlHasIdConverter.COL_ID,
                String.join(",", idStrings)
            ));
        }

        if (filter.getChangedSince() != null) {
            var dialect = this.connectionFactory.getSqlDialect();
            var dateString = SqlHelper.getToDate(dialect, filter.getChangedSince());
            conditions.add(String.format("(%s >= %s OR %s >= %s)",
                SqlTarifkanteElementConverter.COL_CREATED_AT,
                dateString,
                SqlTarifkanteElementConverter.COL_MODIFIED_AT,
                dateString
            ));
        }

        if (conditions.size() > 0) {
            return " WHERE " + String.join(" AND ", conditions);
        } else {
            return "";
        }
    }
}
