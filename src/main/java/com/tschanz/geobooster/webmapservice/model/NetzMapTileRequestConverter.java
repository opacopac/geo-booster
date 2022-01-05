package com.tschanz.geobooster.webmapservice.model;

import com.tschanz.geobooster.netz_maptile.model.NetzMapTileRequest;


public class NetzMapTileRequestConverter {
    public static NetzMapTileRequest fromMapRequest(GetMapRequest mapRequest) {
        return new NetzMapTileRequest(
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
            mapRequest.getViewparams().isShowTerminiert(),
            mapRequest.isTransparent()
        );
    }
}
