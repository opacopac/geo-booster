package com.tschanz.geobooster.netz_repo.service;

import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;

import java.time.LocalDate;
import java.util.Collection;


public interface LinieVarianteRepo {
    Collection<VerkehrskanteVersion> searchLinienVarianteVerkehrskantenVersions(Collection<Long> linienVarianteIds, LocalDate date);
}
