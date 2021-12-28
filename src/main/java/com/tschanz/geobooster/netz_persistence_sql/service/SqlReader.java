package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz_persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlConnectionFactory;
import com.tschanz.geobooster.util.model.Timer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;


@RequiredArgsConstructor
public class SqlReader<T, K> {
    private static final Logger logger = LogManager.getLogger(SqlReader.class);

    private final SqlConnectionFactory connectionFactory;
    private final SqlResultsetConverter<T> resultsetConverter;
    private final String logText;
    private final int logIntervalSec;


    @SneakyThrows
    public Collection<T> read(String query) {
        logger.debug(String.format("executing query %s", query));

        var entries = new ArrayList<T>();
        var connection = connectionFactory.createConnection();

        if (connection.getStatement().execute(query)) {
            var i = 0;
            var timer = new Timer();
            while (connection.getStatement().getResultSet().next()) {
                i++;
                if (timer.checkSecElapsed(logIntervalSec)) {
                    logger.info(String.format(logText, i));
                }

                var resultSet = connection.getStatement().getResultSet();
                var entry = resultsetConverter.fromResultSet(resultSet);
                entries.add(entry);
            }
        }

        connection.closeAll();

        return entries;
    }
}
