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

import java.util.*;


@Service
@RequiredArgsConstructor
public class TarifkanteSqlRepo implements TarifkantePersistenceRepo {
    private static final Logger logger = LogManager.getLogger(TarifkanteSqlRepo.class);

    private final SqlConnectionFactory connectionFactory;


    @Override
    @SneakyThrows
    public Collection<Tarifkante> readAllElements() {
        var sqlReader = new SqlReader<>(
            this.connectionFactory,
            new SqlTarifkanteElementConverter(),
            "%d tk elements loaded...",
            2
        );
        var query = String.format(
            "SELECT %s FROM N_TARIFKANTE_E",
            String.join(",", SqlTarifkanteElementConverter.ALL_COLS)
        );

        return sqlReader.read(query);
    }


    @Override
    @SneakyThrows
    public Collection<TarifkanteVersion> readAllVersions() {
        var tkVkMap = this.readTkVkMap();
        var sqlReader = new SqlReader<>(
            this.connectionFactory,
            new SqlTarifkanteVersionConverter(tkVkMap),
            "%d tk versions loaded...",
            2
        );
        var query = String.format(
            "SELECT %s FROM N_TARIFKANTE_V",
            String.join(",", SqlTarifkanteVersionConverter.ALL_COLS)
        );

        return sqlReader.read(query);
    }


    @SneakyThrows
    private Map<Long, List<Long>> readTkVkMap() {
        var connection = this.connectionFactory.createConnection();
        var query = "SELECT ID_TARIFKANTE_V, ID_VERKEHRS_KANTE_E FROM N_TARIFKANTE_X_N_VERK_KANTE_E";

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
}
