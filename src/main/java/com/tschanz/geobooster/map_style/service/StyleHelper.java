package com.tschanz.geobooster.map_style.service;

import com.tschanz.geobooster.graphics.model.GbLineStyle;
import com.tschanz.geobooster.graphics.model.GbPointStyle;
import com.tschanz.geobooster.map_style.model.*;

import java.util.Collection;


public class StyleHelper {
    public static GbPointStyle getPointStyle(MapStyle mapStyle, float zoomLevel) {
        switch (mapStyle) {
            case WEGANGABE_HALTESTELLEN:
                return WaHaltestelleStyle.getStyle(zoomLevel);
            case HALTESTELLEN:
            default:
                return HaltestelleStyle.getStyle(zoomLevel);
        }
    }


    public static GbLineStyle getLineStyle(MapStyle mapStyle, float zoomLevel) {
        switch (mapStyle) {
            case KANTEN_BLUE:
                return KanteBlueStyle.getStyle(zoomLevel);
            case KANTEN_BLUE_BOLD_DASHED:
                return KanteBlueBoldDashedStyle.getStyle(zoomLevel);
            case KANTEN_BLACK_DASHED:
                return KanteBlackDashedStyle.getStyle(zoomLevel);
            case KANTEN_BLACK:
            case KANTEN_BLACK_BOLD: // TODO
            default:
                return KanteBlackStyle.getStyle(zoomLevel);
        }
    }


    public static GbPointStyle getPointStyle(Collection<MapStyle> mapStyles, float zoomLevel) {
        var style = mapStyles.stream().findFirst().orElseThrow();
        return getPointStyle(style, zoomLevel);
    }


    public static GbLineStyle getLineStyle(Collection<MapStyle> mapStyles, float zoomLevel) {
        var style = mapStyles.stream().findFirst().orElseThrow();
        return getLineStyle(style, zoomLevel);
    }
}
