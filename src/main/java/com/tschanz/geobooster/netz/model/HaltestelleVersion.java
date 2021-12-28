package com.tschanz.geobooster.netz.model;

import com.tschanz.geobooster.geofeature.model.Epsg4326Coordinate;
import com.tschanz.geobooster.versioning.model.Version;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;


@Getter
@RequiredArgsConstructor
public class HaltestelleVersion implements Version {
    private final long id;
    private final long elementId;
    private final LocalDate gueltigVon;
    private final LocalDate gueltigBis;
    private final String name;
    private final Epsg4326Coordinate coordinate;
}
