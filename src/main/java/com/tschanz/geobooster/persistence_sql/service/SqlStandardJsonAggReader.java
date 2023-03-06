package com.tschanz.geobooster.persistence_sql.service;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardJsonAggMapping;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class SqlStandardJsonAggReader {
    private static final Logger logger = LogManager.getLogger(SqlStandardJsonAggReader.class);
    private static final String COL_CLOB = "JSONAGGCLOB";

    private final SqlJdbcTemplateFactory jdbcTemplateFactory;


    @SneakyThrows
    @Retryable(value = SQLRecoverableException.class, maxAttempts = 10, backoff = @Backoff(delay = 3000))
    public <T, F extends SqlFilter<K>, K> List<T> read(SqlStandardJsonAggMapping<T, F, K> jsonAggMapping) {
        var query = this.createQuery(jsonAggMapping);
        logger.info(String.format("executing query '%s'", query));

        try {
            var entries = new ArrayList<T>();
            var jdbcTemplate = jdbcTemplateFactory.getJdbcTemplate();
            var clobs = jdbcTemplate.query(query, new ClobResultsetMapper());

            if (clobs.size() != 1 || clobs.get(0) == null) {
                return Collections.emptyList();
            } else {
                var clob = clobs.get(0);
                var jsonStream = clob.getCharacterStream();

                JsonReader reader = new JsonReader(jsonStream);
                reader.beginArray();
                while (reader.hasNext()) {
                    reader.beginArray();
                    var entry = jsonAggMapping.fromJsonAgg(reader);
                    entries.add(entry);
                    reader.endArray();
                }
                reader.endArray();
                reader.close();
            }

            logger.info(String.format("SUCCESS %d entries read for query '%s'", entries.size(), query));

            return entries;
        } catch (Exception e) {
            logger.error(String.format("ERROR while executing query '%s'", query));
            logger.error(e);

            throw e;
        }
    }


    private static class ClobResultsetMapper implements RowMapper<Clob> {
        @Override
        public Clob mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return resultSet.getClob(COL_CLOB);
        }
    }


    private <T, F extends SqlFilter<K>, K> String createQuery(SqlStandardJsonAggMapping<T, F, K> jsonAggMapping) {
        return String.format(
            "SELECT JSON_ARRAYAGG(JSON_ARRAY(%s NULL ON NULL) RETURNING CLOB) AS %s FROM %s %s",
            String.join(",", jsonAggMapping.getSelectFields()),
            COL_CLOB,
            jsonAggMapping.getTable(),
            this.createWhereClause(jsonAggMapping.getFilters())
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
