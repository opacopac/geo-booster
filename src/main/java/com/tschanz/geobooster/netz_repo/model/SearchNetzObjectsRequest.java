package com.tschanz.geobooster.netz_repo.model;

import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;

import java.time.LocalDate;
import java.util.List;


public interface SearchNetzObjectsRequest {
    LocalDate getDate();
    Extent getBbox();
    float getZoomLevel();
    List<VerkehrsmittelTyp> getVmTypes();
    List<Long> getVerwaltungVersionIds();
    List<Long> getLinieVarianteIds();
    boolean isShowHaltestellen();
    boolean isShowVerkehrskanten();
    boolean isShowTarifkanten();
    boolean isShowUnmappedTarifkanten();
    boolean isShowTerminiert();
}
