package com.tschanz.geobooster;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.retry.annotation.EnableRetry;


@Getter
@Setter
@Configuration
@PropertySource("file:./geo-booster.properties")
@ConfigurationProperties(prefix = "app")
@EnableRetry
public class AppProperties {
    private boolean useServerMode;
    private int serverModeConnectionId;
}
