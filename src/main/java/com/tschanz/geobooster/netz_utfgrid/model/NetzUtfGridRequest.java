package com.tschanz.geobooster.netz_utfgrid.model;

import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Getter
@RequiredArgsConstructor
public class NetzUtfGridRequest {
    private final LocalDate date;
    private final Extent bbox;
    private final int width;
    private final int height;
    private final float zoomLevel;
    private final List<VerkehrsmittelTyp> vmTypes;
    private final List<Long> verwaltungVersionIds;
    private final boolean showHaltestellen;
    private final boolean showVerkehrskanten;
    private final boolean showTarifkanten;
    private final boolean showTerminiert;
}
