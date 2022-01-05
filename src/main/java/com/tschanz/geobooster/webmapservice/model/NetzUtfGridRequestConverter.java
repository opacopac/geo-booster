package com.tschanz.geobooster.webmapservice.model;

import com.tschanz.geobooster.netz_utfgrid.model.NetzUtfGridRequest;


public class NetzUtfGridRequestConverter {
    public static NetzUtfGridRequest fromMapRequest(GetMapRequest mapRequest) {
        return new NetzUtfGridRequest(
            mapRequest.getViewparams().getDate(),
            mapRequest.getBbox(),
            mapRequest.getWidth(),
            mapRequest.getHeight(),
            mapRequest.getZoomLevel(),
            mapRequest.getViewparams().getTypes(),
            mapRequest.getViewparams().getVerwaltungVersionIds(),
            mapRequest.getViewparams().getLinienVariantenIds(),
            mapRequest.hasLayerHaltestellen(),
            mapRequest.hasLayerVerkehrskanten(),
            mapRequest.hasLayerTarifkanten(),
            mapRequest.hasLayerUnmappedTarifkanten(),
            mapRequest.getViewparams().isShowTerminiert()
        );
    }
}
