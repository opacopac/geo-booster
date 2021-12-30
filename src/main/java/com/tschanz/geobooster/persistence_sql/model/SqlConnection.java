package com.tschanz.geobooster.persistence_sql.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.Statement;


@Getter
@RequiredArgsConstructor
public class SqlConnection {
    private final Connection connection;
    private final Statement statement;
    private final SqlConnectionProperties connectionProperties;


    @SneakyThrows
    public void closeAll() {
        this.statement.getResultSet().close();
        this.statement.close();
        this.connection.close();
    }
}
