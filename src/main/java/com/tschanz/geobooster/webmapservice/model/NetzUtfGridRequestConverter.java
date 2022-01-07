package com.tschanz.geobooster.webmapservice.model;

import com.tschanz.geobooster.utfgrid_composer.model.UtfGridRequest;


public class NetzUtfGridRequestConverter {
    public static UtfGridRequest fromMapRequest(GetMapRequest mapRequest) {
        return new UtfGridRequest(
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
            mapRequest.getMapStyles()
        );
    }
}
