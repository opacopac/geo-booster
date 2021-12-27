package com.tschanz.geobooster.netz_graphics.model;

import com.tschanz.geobooster.graphics.model.GbColor;
import com.tschanz.geobooster.graphics.model.GbLineStyle;
import com.tschanz.geobooster.graphics.model.GbStroke;


public class VerkehrskanteStyle {
    private final static VariableWidth width = new VariableWidth(4, 11, 0, 5);


    public static GbLineStyle getStyle(float zoomLevel) {
        return new GbLineStyle(
            GbColor.BLACK,
            new GbStroke(width.getWidth(zoomLevel))
        );
    }
}
