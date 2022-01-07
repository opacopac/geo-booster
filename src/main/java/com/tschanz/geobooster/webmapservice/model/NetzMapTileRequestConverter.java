package com.tschanz.geobooster.webmapservice.model;

import com.tschanz.geobooster.map_tile_composer.model.MapTileRequest;


public class NetzMapTileRequestConverter {
    public static MapTileRequest fromMapRequest(GetMapRequest mapRequest) {
        return new MapTileRequest(
            mapRequest.getViewparams().getDate(),
            mapRequest.getBbox(),
            mapRequest.getWidth(),
            mapRequest.getHeight(),
            mapRequest.getZoomLevel(),
            mapRequest.getViewparams().getAwbVersionId(),
            mapRequest.getViewparams().getTypes(),
            mapRequest.getViewparams().getVerwaltungVersionIds(),
            mapRequest.getViewparams().getLinienVariantenIds(),
            mapRequest.getViewparams().isShowTerminiert(),
            mapRequest.getMapLayerTypes(),
            mapRequest.getMapStyles(),
            mapRequest.isTransparent()
        );
    }
}
