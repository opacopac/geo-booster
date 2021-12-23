package com.tschanz.geobooster.netz.service;

import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;

import java.time.LocalDate;
import java.util.List;


public interface TarifkanteRepo {
    List<TarifkanteVersion> readVersions(LocalDate date, Extent extent, List<VerkehrsmittelTyp> vmTypes);
}
