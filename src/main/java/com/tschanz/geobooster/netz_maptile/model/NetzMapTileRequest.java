package com.tschanz.geobooster.netz_maptile.model;

import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;
import com.tschanz.geobooster.search.model.SearchNetzObjectsRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Getter
@RequiredArgsConstructor
public class NetzMapTileRequest implements SearchNetzObjectsRequest {
    private final LocalDate date;
    private final Extent bbox;
    private final int width;
    private final int height;
    private final float zoomLevel;
    private final List<VerkehrsmittelTyp> vmTypes;
    private final List<Long> verwaltungVersionIds;
    private final List<Long> linieVarianteIds;
    private final boolean showHaltestellen;
    private final boolean showVerkehrskanten;
    private final boolean showTarifkanten;
    private final boolean showUnmappedTarifkanten;
    private final boolean showTerminiert;
    private final boolean isBgTransparent;
}
