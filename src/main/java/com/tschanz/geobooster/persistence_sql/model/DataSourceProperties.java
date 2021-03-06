package com.tschanz.geobooster.persistence_sql.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Getter
@Setter
@Configuration
@PropertySource("file:./geo-booster.properties")
@ConfigurationProperties(prefix = "datasource")
public class DataSourceProperties {
    private int fetchSize;
    private boolean useJsonAgg;
    private SqlConnectionProperties[] connection;
}
