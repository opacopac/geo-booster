package com.tschanz.geobooster.netz_repo.service;

import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;

import java.time.LocalDate;
import java.util.Collection;


public interface LinieVarianteRepo {
    Collection<VerkehrskanteVersion> searchVerkehrskanteVersions(
        Collection<Long> linienVarianteIds,
        Collection<VerkehrsmittelTyp> vmTypes,
        LocalDate date
    );


    Collection<TarifkanteVersion> searchTarifkanteVersions(
        Collection<Long> linienVarianteIds,
        Collection<VerkehrsmittelTyp> vmTypes,
        LocalDate date
    );
}