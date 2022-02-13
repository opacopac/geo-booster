package com.tschanz.geobooster.utfgrid_composer.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Getter
@Setter
@Configuration
@PropertySource("file:./geo-booster.properties")
@ConfigurationProperties(prefix = "utfgrid")
public class UtfGridProperties {
    private int lineMinLengthChars;
    private int maxPointEntries;
    private int maxLineEntries;
}
