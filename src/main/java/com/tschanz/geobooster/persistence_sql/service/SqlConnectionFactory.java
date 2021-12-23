package com.tschanz.geobooster.persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.model.SqlConnection;
import com.tschanz.geobooster.persistence_sql.model.SqlDialect;
import com.tschanz.geobooster.persistence_sql.model.SqlRepoProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.sql.DriverManager;


@Service
@RequiredArgsConstructor
public class SqlConnectionFactory {
    private final SqlRepoProperties properties;

    @SneakyThrows
    public SqlConnection createConnection() {
        var connection = DriverManager.getConnection(
            properties.getUrl(),
            properties.getUsername(),
            properties.getPassword()
        );
        connection.setSchema(properties.getSchema());

        var sqlDialect = SqlDialect.valueOf(properties.getSqldialect());
        var statement = connection.createStatement();

        return new SqlConnection(connection, statement, sqlDialect);
    }
}
