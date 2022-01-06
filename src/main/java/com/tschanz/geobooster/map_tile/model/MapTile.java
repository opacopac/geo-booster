package com.tschanz.geobooster.map_tile.model;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Getter
@RequiredArgsConstructor
public class MapTile {
    @NonNull private final int width;
    @NonNull private final int height;
    @NonNull private final boolean isBgTransparent;
    @NonNull private final Epsg3857Coordinate minCoordinate;
    @NonNull private final Epsg3857Coordinate maxCoordinate;
    private final List<MapTilePoint> points;
    private final List<MapTileLine> lines;
}
