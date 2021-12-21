package com.tschanz.geobooster.persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.model.SqlConnection;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.sql.DriverManager;


@Service
public class SqlConnectionFactory {
    @SneakyThrows
    public SqlConnection createConnection() {
        var connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/test",
            "geobooster",
            "geobooster"
        );
        var statement = connection.createStatement();

        return new SqlConnection(connection, statement);
    }
}
