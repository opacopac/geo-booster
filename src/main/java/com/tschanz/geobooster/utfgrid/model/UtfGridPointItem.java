package com.tschanz.geobooster.utfgrid.model;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.util.model.Tuple2;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Getter
@RequiredArgsConstructor
public class UtfGridPointItem implements UtfGridItem {
    private final Epsg3857Coordinate coordinate;
    private final float width;
    private final List<Tuple2<String, ?>> dataFields;
}
