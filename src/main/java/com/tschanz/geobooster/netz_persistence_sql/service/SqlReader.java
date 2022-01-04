package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz_persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;


@RequiredArgsConstructor
public class SqlReader<T> {
    private static final Logger logger = LogManager.getLogger(SqlReader.class);

    private final SqlConnectionFactory connectionFactory;
    private final SqlResultsetConverter<T> resultsetConverter;


    @SneakyThrows
    public Collection<T> read(String query) {
        logger.debug(String.format("executing query %s", query));

        var entries = new ArrayList<T>();
        var connection = connectionFactory.getConnection();

        if (connection.getStatement().execute(query)) {
            while (connection.getStatement().getResultSet().next()) {
                var resultSet = connection.getStatement().getResultSet();
                var entry = resultsetConverter.fromResultSet(resultSet);
                entries.add(entry);
            }
        }

        connection.closeResultsetAndStatement();

        return entries;
    }
}
