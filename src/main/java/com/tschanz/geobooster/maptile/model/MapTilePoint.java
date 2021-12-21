package com.tschanz.geobooster.maptile.model;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.graphics.model.GbPointStyle;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class MapTilePoint {
    private final Epsg3857Coordinate coordinate;
    private final GbPointStyle style;
}
