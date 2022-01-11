package com.tschanz.geobooster.persistence_sql.service;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlJsonAggConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class SqlJsonAggReader {
    private static final Logger logger = LogManager.getLogger(SqlJsonAggReader.class);
    private static final String COL_CLOB = "JSONAGGCLOB";

    private final SqlConnectionFactory connectionFactory;


    @SneakyThrows
    public <T> List<T> read(SqlJsonAggConverter<T> jsonAggConverter) {
        var query = this.createQuery(jsonAggConverter.getTable(), jsonAggConverter.getFields());
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


    private String createQuery(String table, String[] fields) {
        return String.format(
            "SELECT JSON_ARRAYAGG(JSON_ARRAY(%s NULL ON NULL) RETURNING CLOB) AS %s FROM %s",
            String.join(",", fields),
            COL_CLOB,
            table
        );
    }
}
