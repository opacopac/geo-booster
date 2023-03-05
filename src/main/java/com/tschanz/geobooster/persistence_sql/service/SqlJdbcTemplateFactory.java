package com.tschanz.geobooster.persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.model.ConnectionState;
import com.tschanz.geobooster.persistence_sql.model.SqlConnectionProperties;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SqlJdbcTemplateFactory {
    private final ConnectionState connectionState;

    private SqlConnectionProperties currentProperties;
    private JdbcTemplate currentJdbcTemplate;


    @SneakyThrows
    public JdbcTemplate getJdbcTemplate() {
        var properties = this.connectionState.getSelectedConnectionProperties();

        if (this.currentJdbcTemplate == null || !this.currentProperties.equals(properties)) {
            var dataSource = new HikariDataSource();
            dataSource.setJdbcUrl(properties.getUrl());
            dataSource.setUsername(properties.getUsername());
            dataSource.setPassword(properties.getPassword());
            dataSource.setSchema(properties.getSchema());

            this.currentProperties = properties;
            this.currentJdbcTemplate = new JdbcTemplate(dataSource);
            this.currentJdbcTemplate.setFetchSize(this.connectionState.getFetchSize());
        }

        return this.currentJdbcTemplate;
    }
}
