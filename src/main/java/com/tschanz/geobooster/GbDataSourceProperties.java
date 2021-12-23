package com.tschanz.geobooster;

import com.tschanz.geobooster.persistence_sql.model.SqlRepoProperties;
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
public class GbDataSourceProperties implements SqlRepoProperties {
    private String url;
    private String username;
    private String password;
}
