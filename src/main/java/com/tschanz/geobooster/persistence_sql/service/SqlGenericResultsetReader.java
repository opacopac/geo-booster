package com.tschanz.geobooster.persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.model.SqlGenericResultsetMapping;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.sql.SQLRecoverableException;
import java.util.List;


@Component
@RequiredArgsConstructor
public class SqlGenericResultsetReader {
    private static final Logger logger = LogManager.getLogger(SqlGenericResultsetReader.class);

    private final SqlJdbcTemplateFactory dataSourceFactory;


    @SneakyThrows
    @Retryable(value = SQLRecoverableException.class, maxAttempts = 5, backoff = @Backoff(delay = 3000))
    public <T> List<T> read(SqlGenericResultsetMapping<T> mapping) {
        var query = mapping.getSelectQuery();
        logger.info(String.format("executing query '%s'", query));

        try {
            var jdbcTemplate = dataSourceFactory.getJdbcTemplate();
            var entries = jdbcTemplate.query(query, mapping);
            logger.info(String.format("SUCCESS %d entries read for query '%s'", entries.size(), query));

            return entries;
        } catch (Exception e) {
            logger.error(String.format("ERROR while executing query '%s'", query));
            logger.error(e);

            throw e;
        }
    }
}
