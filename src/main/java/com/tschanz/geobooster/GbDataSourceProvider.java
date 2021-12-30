package com.tschanz.geobooster;

import com.tschanz.geobooster.persistence_sql.model.SqlConnectionProperties;
import com.tschanz.geobooster.persistence_sql.service.SqlConnectionPropertyProvider;
import com.tschanz.geobooster.presentation.service.ConnectionPropertySelector;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class GbDataSourceProvider implements SqlConnectionPropertyProvider, ConnectionPropertySelector {
    private final GbDataSourceProperties dataSourceProperties;
    private SqlConnectionProperties selectedConnection;


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
