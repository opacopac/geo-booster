package com.tschanz.geobooster.persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.model.SqlFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardResultsetMapping;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.sql.SQLRecoverableException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class SqlStandardResultsetReader {
    private static final Logger logger = LogManager.getLogger(SqlStandardResultsetReader.class);

    private final SqlJdbcTemplateFactory jdbcTemplateFactory;


    @SneakyThrows
    @Retryable(value = SQLRecoverableException.class, maxAttempts = 10, backoff = @Backoff(delay = 3000))
    public <T, F extends SqlFilter<K>, K> List<T> read(SqlStandardResultsetMapping<T, F, K> mapping) {
        var query = this.createQuery(mapping);
        logger.info(String.format("executing query '%s'", query));

        try {
            var jdbcTemplate = jdbcTemplateFactory.getJdbcTemplate();
            var entries = jdbcTemplate.query(query, mapping);
            logger.info(String.format("SUCCESS %d entries read for query '%s'", entries.size(), query));

            return entries;
        } catch (Exception e) {
            logger.error(String.format("ERROR while executing query '%s'", query));
            logger.error(e);

            throw e;
        }
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
