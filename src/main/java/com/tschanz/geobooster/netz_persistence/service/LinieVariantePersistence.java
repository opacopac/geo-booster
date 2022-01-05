package com.tschanz.geobooster.netz_persistence.service;

import java.util.Collection;


public interface LinieVariantePersistence {
    Collection<Long> searchVerkehrskantenIdsByLinienVarianteIds(Collection<Long> linieVarianteIds);
}
