package com.tschanz.geobooster.netz.service;

import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.Verkehrskante;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public interface VerkehrskanteRepo {
    List<VerkehrskanteVersion> readVersions(LocalDate date, Extent extent, List<VerkehrsmittelTyp> vmTypes);

    Map<Long, Verkehrskante> getElementMap();

    Map<Long, VerkehrskanteVersion> getVersionMap();
}
