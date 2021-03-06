package com.tschanz.geobooster.persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.model.SqlFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardResultsetMapping;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class SqlStandardResultsetReader {
    private static final Logger logger = LogManager.getLogger(SqlStandardResultsetReader.class);

    private final SqlConnectionFactory connectionFactory;


    @SneakyThrows
    public <T, F extends SqlFilter<K>, K> List<T> read(SqlStandardResultsetMapping<T, F, K> mapping) {
        var query = this.createQuery(mapping);
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


    private <T, F extends SqlFilter<K>, K> String createQuery(SqlStandardResultsetMapping<T, F, K> mapping) {
        return String.format(
            "SELECT %s FROM %s %s",
            String.join(",", mapping.getSelectFields()),
            mapping.getTable(),
            this.createWhereClause(mapping.getFilters())
        );
    }


    private <F extends SqlFilter<K>, K> String createWhereClause(Collection<F> filters) {
        var conditions = new ArrayList<String>();

        for (var filter: filters) {
            if (!filter.getFilterValues().isEmpty()) {
                var valueStrings = filter.getFilterValues().stream().map(filter::escapeValue).collect(Collectors.toList());
                var condition = String.format(
                    "%s IN (%s)",
                    filter.getColumn(),
                    String.join(",", valueStrings)
                );
                conditions.add(condition);
            }
        }

        return conditions.isEmpty() ? "" : String.format(
            "WHERE %s",
            String.join(" AND ", conditions)
        );
    }
}
