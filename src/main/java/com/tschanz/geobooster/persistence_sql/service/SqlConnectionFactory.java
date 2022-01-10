package com.tschanz.geobooster.persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.model.ConnectionState;
import com.tschanz.geobooster.persistence_sql.model.SqlConnection;
import com.tschanz.geobooster.persistence_sql.model.SqlConnectionProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;


@Service
@RequiredArgsConstructor
public class SqlConnectionFactory {
    private final static int CONNECTION_REUSE_TIMEOUT_SEC = 5;

    private final ConnectionState connectionState;

    private SqlConnectionProperties currentProperties;
    private Connection currentConnection;


    @SneakyThrows
    public SqlConnection getConnection() {
        var properties = this.connectionState.getSelectedConnectionProperties();

        Connection connection;
        if (this.currentConnection != null
            && this.currentProperties.equals(properties)
            && this.currentConnection.isValid(CONNECTION_REUSE_TIMEOUT_SEC)
        ) {
            connection = this.currentConnection;
        } else {
            connection = DriverManager.getConnection(
                properties.getUrl(),
                properties.getUsername(),
                properties.getPassword()
            );
            connection.setSchema(properties.getSchema());
            this.currentConnection = connection;
            this.currentProperties = properties;
        }

        var statement = connection.createStatement();
        statement.setFetchSize(this.connectionState.getFetchSize());

        return new SqlConnection(connection, statement, properties);
    }
}
