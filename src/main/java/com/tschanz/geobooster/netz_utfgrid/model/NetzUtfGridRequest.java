package com.tschanz.geobooster.netz_utfgrid.model;

import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.map_layer.model.MapLayer;
import com.tschanz.geobooster.map_layer.model.MapLayerRequest;
import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;


@Getter
@RequiredArgsConstructor
public class NetzUtfGridRequest implements MapLayerRequest {
    private final LocalDate date;
    private final Extent bbox;
    private final int width;
    private final int height;
    private final float zoomLevel;
    private final long awbVersionId;
    private final Collection<VerkehrsmittelTyp> vmTypes;
    private final Collection<Long> verwaltungVersionIds;
    private final Collection<Long> linieVarianteIds;
    private final boolean showTerminiert;
    private final Collection<MapLayer> mapLayers;
}
