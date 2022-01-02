package com.tschanz.geobooster.persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.model.ConnectionState;
import com.tschanz.geobooster.persistence_sql.model.SqlConnection;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.sql.DriverManager;


@Service
@RequiredArgsConstructor
public class SqlConnectionFactory {
    private final ConnectionState connectionState;
    private final static int FETCH_SIZE = 10000;


    @SneakyThrows
    public SqlConnection createConnection() {
        var properties = this.connectionState.getConnectionProperties();
        if (properties == null) {
            throw new IllegalArgumentException("no connection selected");
        }
        
        var connection = DriverManager.getConnection(
            properties.getUrl(),
            properties.getUsername(),
            properties.getPassword()
        );
        connection.setSchema(properties.getSchema());

        var statement = connection.createStatement();
        statement.setFetchSize(FETCH_SIZE);

        return new SqlConnection(connection, statement, properties);
    }
}
