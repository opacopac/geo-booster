package com.tschanz.geobooster.map_tile.model;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.graphics.model.GbLineStyle;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class MapTileLine {
    private final Epsg3857Coordinate startCoordinate;
    private final Epsg3857Coordinate endCoordinate;
    private final GbLineStyle style;
}
