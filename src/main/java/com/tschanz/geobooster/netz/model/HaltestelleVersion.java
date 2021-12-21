package com.tschanz.geobooster.netz.model;

import com.tschanz.geobooster.geofeature.model.Epsg4326Coordinate;
import com.tschanz.geobooster.versioning.model.Version;
import com.tschanz.geobooster.versioning.model.VersionInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class HaltestelleVersion implements Version<Haltestelle> {
    private final VersionInfo<Haltestelle> versionInfo;
    private final String name;
    private final Epsg4326Coordinate coordinate;
}
