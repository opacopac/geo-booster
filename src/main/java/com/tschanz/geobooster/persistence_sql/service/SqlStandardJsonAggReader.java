package com.tschanz.geobooster.persistence_sql.service;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardJsonAggConverter;
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
public class SqlStandardJsonAggReader {
    private static final Logger logger = LogManager.getLogger(SqlStandardJsonAggReader.class);
    private static final String COL_CLOB = "JSONAGGCLOB";

    private final SqlConnectionFactory connectionFactory;


    @SneakyThrows
    public <T, F extends SqlFilter<K>, K> List<T> read(SqlStandardJsonAggConverter<T, F, K> jsonAggConverter) {
        var query = this.createQuery(jsonAggConverter);
        logger.info(String.format("executing query '%s'", query));

        var entries = new ArrayList<T>();
        var connection = connectionFactory.getConnection();
        if (connection.getStatement().execute(query)) {
            connection.getStatement().getResultSet().next();
            var resultSet = connection.getStatement().getResultSet();
            var jsonStream = resultSet.getClob(COL_CLOB).getCharacterStream();

            JsonReader reader = new JsonReader(jsonStream);
            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginArray();
                var entry = jsonAggConverter.fromJsonAgg(reader);
                entries.add(entry);
                reader.endArray();
            }
            reader.endArray();
            reader.close();
        }
        connection.closeResultsetAndStatement();

        logger.info(String.format("%d entries read", entries.size()));

        return entries;
    }


    private <T, F extends SqlFilter<K>, K> String createQuery(SqlStandardJsonAggConverter<T, F, K> jsonAggConverter) {
        return String.format(
            "SELECT JSON_ARRAYAGG(JSON_ARRAY(%s NULL ON NULL) RETURNING CLOB) AS %s FROM %s %s",
            String.join(",", jsonAggConverter.getSelectFields()),
            COL_CLOB,
            jsonAggConverter.getTable(),
            this.createWhereClause(jsonAggConverter.getFilters())
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
