package com.tschanz.geobooster.persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.model.SqlGenericResultsetMapping;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class SqlGenericResultsetReader {
    private static final Logger logger = LogManager.getLogger(SqlGenericResultsetReader.class);

    private final SqlConnectionFactory connectionFactory;


    @SneakyThrows
    public <T> List<T> read(SqlGenericResultsetMapping<T> mapping) {
        var query = mapping.getSelectQuery();
        logger.info(String.format("executing query '%s'", query));

        var entries = new ArrayList<T>();
        var connection = connectionFactory.getConnection();

        if (connection.getStatement().execute(query)) {
            while (connection.getStatement().getResultSet().next()) {
                var resultSet = connection.getStatement().getResultSet();
                var entry = mapping.fromResultSet(resultSet);
                entries.add(entry);
            }
        }
        connection.closeResultsetAndStatement();

        logger.info(String.format("%d entries read", entries.size()));

        return entries;
    }
}
