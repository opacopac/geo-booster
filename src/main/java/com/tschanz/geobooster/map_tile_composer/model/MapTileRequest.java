package com.tschanz.geobooster.map_tile_composer.model;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.map_layer.model.MapLayerRequest;
import com.tschanz.geobooster.map_layer.model.MapLayerType;
import com.tschanz.geobooster.map_style.model.MapStyle;
import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;


@Getter
@RequiredArgsConstructor
public class MapTileRequest implements MapLayerRequest {
    private final LocalDate date;
    private final Extent<Epsg3857Coordinate> bbox;
    private final int width;
    private final int height;
    private final float zoomLevel;
    private final long awbVersionId;
    private final Collection<VerkehrsmittelTyp> vmTypes;
    private final Collection<Long> verwaltungVersionIds;
    private final Collection<Long> linieVarianteIds;
    private final boolean showTerminiert;
    private final Collection<MapLayerType> mapLayerTypes;
    private final Collection<MapStyle> mapStyles;
    private final boolean isBgTransparent;
}
