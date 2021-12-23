package com.tschanz.geobooster.utfgrid.model;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.util.model.KeyValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Getter
@RequiredArgsConstructor
public class UtfGridLineItem implements UtfGridItem {
    private final Epsg3857Coordinate startCoordinate;
    private final Epsg3857Coordinate endCoordinate;
    private final List<KeyValue<String, ?>> dataFields;
}
