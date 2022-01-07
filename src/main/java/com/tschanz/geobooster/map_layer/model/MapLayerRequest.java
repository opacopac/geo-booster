package com.tschanz.geobooster.map_layer.model;

import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;

import java.time.LocalDate;
import java.util.Collection;


public interface MapLayerRequest extends HaltestelleLayerRequest, VerkehrskanteLayerRequest, TarifkanteLayerRequest,
    UnmappedTarifkanteLayerRequest, HaltestelleWegangabeLayerRequest, AwbVkLayerRequest, AwbTkLayerRequest
{
    LocalDate getDate();
    Extent getBbox();
    long getAwbVersionId();
    Collection<VerkehrsmittelTyp> getVmTypes();
    Collection<Long> getVerwaltungVersionIds();
    Collection<Long> getLinieVarianteIds();
    boolean isShowTerminiert();
    Collection<MapLayerType> getMapLayerTypes();
}
