package com.tschanz.geobooster.map_tile_composer.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Getter
@Setter
@Configuration
@PropertySource("file:./geo-booster.properties")
@ConfigurationProperties(prefix = "maptile")
public class MapTileProperties {
    private int lineMinLengthPx;
}
