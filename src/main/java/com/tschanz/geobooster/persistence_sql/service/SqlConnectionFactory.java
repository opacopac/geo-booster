package com.tschanz.geobooster.persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.model.SqlConnection;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.sql.DriverManager;


@Service
@RequiredArgsConstructor
public class SqlConnectionFactory {
    private final SqlConnectionPropertyProvider propertyProvider;


    @SneakyThrows
    public SqlConnection createConnection() {
        var properties = this.propertyProvider.getConnectionProperties();
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

        return new SqlConnection(connection, statement, properties);
    }
}
