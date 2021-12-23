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
        return this.getHaltestelle1Version().getCoordinate();
    }


    public Epsg4326Coordinate getEndCoordinate() {
        return this.getHaltestelle1Version().getCoordinate();
    }


    public Haltestelle getHaltestelle1() {
        return this.versionInfo.getElement().getHaltestelle1();
    }


    public Haltestelle getHaltestelle2() {
        return this.versionInfo.getElement().getHaltestelle2();
    }


    public HaltestelleVersion getHaltestelle1Version() {
        return this.getHaltestelle1()
            .getElementInfo()
            .getVersion(this.versionInfo.getGueltigVon());
    }


    public HaltestelleVersion getHaltestelle2Version() {
        return this.getHaltestelle2()
            .getElementInfo()
            .getVersion(this.versionInfo.getGueltigVon());
    }


    public List<VerkehrsmittelTyp> getVmTypes() {
        return VerkehrsmittelTyp.getVmTypes(this.vmTypeBitmask);
    }


    public boolean hasOneOfVmTypes(List<VerkehrsmittelTyp> vmTypes) {
        return (VerkehrsmittelTyp.getBitMask(vmTypes) & this.vmTypeBitmask) > 0;
    }
}
