package com.tschanz.geobooster.netz.model;

import com.tschanz.geobooster.geofeature.model.Epsg4326Coordinate;
import com.tschanz.geobooster.versioning.model.Version;
import com.tschanz.geobooster.versioning.model.VersionInfo;
import lombok.Getter;

import java.util.List;


@Getter
public class VerkehrskanteVersion implements Version<Verkehrskante> {
    private final VersionInfo<Verkehrskante> versionInfo;
    private final List<Verwaltung> verwaltungen;
    private final byte vmTypeBitmask;


    public VerkehrskanteVersion(
        VersionInfo<Verkehrskante> versionInfo,
        List<Verwaltung> verwaltungen,
        List<VerkehrsmittelTyp> vmTypes
    ) {
        this.versionInfo = versionInfo;
        this.verwaltungen = verwaltungen;
        this.vmTypeBitmask = VerkehrsmittelTyp.getBitMask(vmTypes);
    }


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
