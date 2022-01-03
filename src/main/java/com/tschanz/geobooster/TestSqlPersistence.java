package com.tschanz.geobooster;

import com.tschanz.geobooster.persistence_sql.model.ConnectionState;
import com.tschanz.geobooster.persistence_sql.model.SqlConnectionProperties;
import com.tschanz.geobooster.persistence_sql.service.SqlHelper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class TestSqlPersistence {
    private final static Logger logger = LogManager.getLogger(TestSqlPersistence.class);
    private final static int FETCH_SIZE = 10000;

    private final ConnectionState connectionState;


    @SneakyThrows
    public void test1() {
        logger.info("TEST1");
        var startTimeMs = Instant.now().toEpochMilli();
        var properties = this.connectionState.getConnectionProperties();
        var connection = this.getConnection(properties);
        var statement = this.createStatement(connection);
        var date = SqlHelper.getToDate(properties.getSqldialect(), LocalDate.parse("2021-12-01"));
        var query = String.format("SELECT * FROM N_TARIFKANTE_V WHERE CREATED_AT >= %s OR MODIFIED_AT >= %s", date, date);
        this.executeQuery(statement, query);
        this.readResultSets(statement);
        this.closeStatement(statement);
        this.closeConnection(connection);
        var endTime = Instant.now().toEpochMilli();
        logger.info(String.format("TEST1 done in %d ms", endTime - startTimeMs));
    }


    @SneakyThrows
    public void test2() {
        logger.info("TEST2");
        var startTimeMs = Instant.now().toEpochMilli();
        var properties = this.connectionState.getConnectionProperties();
        var date = SqlHelper.getToDate(properties.getSqldialect(), LocalDate.parse("2021-12-01"));
        var query = String.format("SELECT * FROM N_TARIFKANTE_V WHERE CREATED_AT >= %s OR MODIFIED_AT >= %s", date, date);

        for (int i = 0; i < 3; i++) {
            var connection = this.getConnection(properties);
            var statement = this.createStatement(connection);
            this.executeQuery(statement, query);
            this.readResultSets(statement);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        var endTime = Instant.now().toEpochMilli();
        logger.info(String.format("TEST2 done in %d ms", endTime - startTimeMs));
    }


    @SneakyThrows
    public void test3() {
        logger.info("TEST3");
        var startTimeMs = Instant.now().toEpochMilli();
        var properties = this.connectionState.getConnectionProperties();
        var date = SqlHelper.getToDate(properties.getSqldialect(), LocalDate.parse("2021-12-01"));
        var query = String.format("SELECT * FROM N_TARIFKANTE_V WHERE CREATED_AT >= %s OR MODIFIED_AT >= %s", date, date);

        var connection = this.getConnection(properties);
        for (int i = 0; i < 3; i++) {
            var statement = this.createStatement(connection);
            this.executeQuery(statement, query);
            this.readResultSets(statement);
            this.closeStatement(statement);
        }
        this.closeConnection(connection);

        var endTime = Instant.now().toEpochMilli();
        logger.info(String.format("TEST3 done in %d ms", endTime - startTimeMs));
    }


    @SneakyThrows
    public void test4() {
        logger.info("TEST4");
        var startTimeMs = Instant.now().toEpochMilli();
        var properties = this.connectionState.getConnectionProperties();
        var date = SqlHelper.getToDate(properties.getSqldialect(), LocalDate.parse("2021-12-01"));
        var query = String.format("SELECT * FROM N_TARIFKANTE_V WHERE CREATED_AT >= %s OR MODIFIED_AT >= %s", date, date);

        var connection = this.getConnection(properties);
        var statement = this.createStatement(connection);
        for (int i = 0; i < 3; i++) {
            this.executeQuery(statement, query);
            this.readResultSets(statement);
        }
        this.closeStatement(statement);
        this.closeConnection(connection);

        var endTime = Instant.now().toEpochMilli();
        logger.info(String.format("TEST4 done in %d ms", endTime - startTimeMs));
    }


    public void test5() {
        logger.info("TEST5");
        var startTimeMs = Instant.now().toEpochMilli();
        var properties = this.connectionState.getConnectionProperties();
        var query = "SELECT SCN_TO_TIMESTAMP(MAX(ora_rowscn)) from N_TARIFKANTE_V";

        var connection = this.getConnection(properties);
        var statement = this.createStatement(connection);
        for (int i = 0; i < 3; i++) {
            this.executeQuery(statement, query);
            this.readResultSets(statement);
        }
        this.closeStatement(statement);
        this.closeConnection(connection);

        var endTime = Instant.now().toEpochMilli();
        logger.info(String.format("TEST5 done in %d ms", endTime - startTimeMs));
    }


    @SneakyThrows
    private Connection getConnection(SqlConnectionProperties properties) {
        logger.info("create connection...");
        var connection = DriverManager.getConnection(
            properties.getUrl(),
            properties.getUsername(),
            properties.getPassword()
        );
        connection.setSchema(properties.getSchema());
        logger.info("create connection done");
        return connection;
    }


    @SneakyThrows
    private Statement createStatement(Connection connection) {
        logger.info("create statement...");
        var statement = connection.createStatement();
        statement.setFetchSize(FETCH_SIZE);
        logger.info("create done...");
        return statement;
    }


    @SneakyThrows
    private void executeQuery(Statement statement, String query) {
        logger.info(String.format("executing query '%s'", query));
        statement.execute(query);
        logger.info("executing query done");
    }


    @SneakyThrows
    private void readResultSets(Statement statement) {
        logger.info("reading result sets...");
        while (statement.getResultSet().next()) {
            var resultSet = statement.getResultSet();
        }
        logger.info("reading result sets done");
    }


    @SneakyThrows
    private void closeStatement(Statement statement) {
        logger.info("closing statement...");
        statement.close();
        logger.info("closing statement done");
    }


    @SneakyThrows
    private void closeConnection(Connection connection) {
        logger.info("closing connection...");
        connection.close();
        logger.info("closing connection done");
    }
}
