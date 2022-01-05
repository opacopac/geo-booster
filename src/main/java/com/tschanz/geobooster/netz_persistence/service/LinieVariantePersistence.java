package com.tschanz.geobooster.netz_persistence.service;

import java.time.LocalDate;
import java.util.Collection;


public interface LinieVariantePersistence {
    Collection<Long> searchVerkehrskanteIds(Collection<Long> linieVarianteIds);

    Collection<Long> searchTarifkanteIds(Collection<Long> linieVarianteIds, LocalDate date);
}
