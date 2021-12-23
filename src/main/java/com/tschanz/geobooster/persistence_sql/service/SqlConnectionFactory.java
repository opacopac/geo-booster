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
    /*
    quickconnection.19 = NOVAP_PREV3_new,JDBC,jdbc:oracle:thin:@porasbbmgmscan.sbb.ch:1551/NOVAP_INTE,NOVAP_PREV3,NOVAP_PREV3,NOVAP_PREV3,
    quickconnection.20 = (AT) local mysql,JDBC,jdbc:mysql://localhost:3306/test,,,,
     */

    @SneakyThrows
    public SqlConnection createConnection() {
        var connection = DriverManager.getConnection(
            properties.getUrl(),
            properties.getUsername(),
            properties.getPassword()
        );

        /*var connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/test",
            "geobooster",
            "geobooster"
        );*/

        /*var connection = DriverManager.getConnection(
            "jdbc:oracle:thin:@porasbbmgmscan.sbb.ch:1551/NOVAP_INTE",
            "NOVAP_PREV3",
            "NOVAP_PREV3"
        );
        connection.setSchema("NOVAP_PREV3"); */

        var statement = connection.createStatement();

        return new SqlConnection(connection, statement, SqlDialect.MYSQL);
    }
}
