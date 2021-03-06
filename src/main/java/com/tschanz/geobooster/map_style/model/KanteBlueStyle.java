package com.tschanz.geobooster.map_style.model;

import com.tschanz.geobooster.graphics.model.GbColor;
import com.tschanz.geobooster.graphics.model.GbLineStyle;
import com.tschanz.geobooster.graphics.model.GbStroke;


public class KanteBlueStyle {
    public final static ZoomVariableWidth WIDTH = new ZoomVariableWidth(4, 11, 0, 5);


    public static GbLineStyle getStyle(float zoomLevel) {
        return new GbLineStyle(
            GbColor.MEDIUM_BLUE,
            new GbStroke(WIDTH.getWidth(zoomLevel))
        );
    }
}
