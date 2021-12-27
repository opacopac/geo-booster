package com.tschanz.geobooster.netz_graphics.model;

import com.tschanz.geobooster.graphics.model.GbColor;
import com.tschanz.geobooster.graphics.model.GbLineStyle;
import com.tschanz.geobooster.graphics.model.GbStroke;


public class VerkehrskanteStyle {
    public final static ZoomVariableWidth WIDTH = new ZoomVariableWidth(4, 11, 0, 5);


    public static GbLineStyle getStyle(float zoomLevel) {
        return new GbLineStyle(
            GbColor.BLACK,
            new GbStroke(WIDTH.getWidth(zoomLevel))
        );
    }
}
