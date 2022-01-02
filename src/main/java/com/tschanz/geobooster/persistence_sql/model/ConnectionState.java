package com.tschanz.geobooster.persistence_sql.model;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class ConnectionState {
    private final DataSourceProperties dataSourceProperties;
    private SqlConnectionProperties selectedConnection;


    public ConnectionState(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }


    public List<String> getConnectionList() {
        return Arrays.stream(this.dataSourceProperties.getConnection())
            .map(SqlConnectionProperties::getTitle)
            .collect(Collectors.toList());
    }


    public void selectConnection(int index) {
        this.selectedConnection = this.dataSourceProperties.getConnection()[index];
    }


    public SqlConnectionProperties getConnectionProperties() {
        return this.selectedConnection;
    }
}
