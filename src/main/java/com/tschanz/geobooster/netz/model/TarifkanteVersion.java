package com.tschanz.geobooster.netz.model;

import com.tschanz.geobooster.geofeature.model.Epsg4326Coordinate;
import com.tschanz.geobooster.versioning.model.Version;
import com.tschanz.geobooster.versioning.model.VersionInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class TarifkanteVersion implements Version<Tarifkante> {
    private final VersionInfo<Tarifkante> versionInfo;
    private final List<Verkehrskante> verkehrskanten;


    public Epsg4326Coordinate getStartCoordinate() {
        return this.getHaltestelle1Version().getCoordinate();
    }


    public Epsg4326Coordinate getEndCoordinate() {
        return this.getHaltestelle2Version().getCoordinate();
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
        return this.verkehrskanten
            .stream()
            .map(vk -> vk.getElementInfo().getVersion(this.versionInfo.getGueltigVon()))
            .filter(Objects::nonNull)
            .flatMap(vkv -> vkv.getVmTypes().stream())
            .collect(Collectors.toList());
    }


    public boolean hasOneOfVmTypes(List<VerkehrsmittelTyp> vmTypes) {
        var vmTypeBitmask = VerkehrsmittelTyp.getBitMask(this.getVmTypes());
        return (VerkehrsmittelTyp.getBitMask(vmTypes) & vmTypeBitmask) > 0;
    }
}
