package com.tschanz.geobooster.netz.model;

import com.tschanz.geobooster.geofeature.model.Epsg4326Coordinate;
import com.tschanz.geobooster.versioning.model.Version;
import com.tschanz.geobooster.versioning.model.VersionInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class TarifkanteVersion implements Version<Tarifkante> {
    private final VersionInfo<Tarifkante> versionInfo;


    public Epsg4326Coordinate getStartCoordinate() {
        var hstElement = this.versionInfo.getElement().getHaltestelle1();
        var hstVersion = hstElement.getElementInfo().getVersion(this.versionInfo.getGueltigVon());

        return hstVersion.getCoordinate();
    }


    public Epsg4326Coordinate getEndCoordinate() {
        var hstElement = this.versionInfo.getElement().getHaltestelle2();
        var hstVersion = hstElement.getElementInfo().getVersion(this.versionInfo.getGueltigVon());

        return hstVersion.getCoordinate();
    }
}
