package com.tschanz.geobooster.netz_graphics.model;

import com.tschanz.geobooster.graphics.model.GbColor;
import com.tschanz.geobooster.graphics.model.GbLineStyle;
import com.tschanz.geobooster.graphics.model.GbStroke;


public class TarifkanteStyle {
    private final static VariableWidth width = new VariableWidth(3, 13, 0, 28);


    public static GbLineStyle getStyle(float zoomLevel) {
        return new GbLineStyle(
            new GbColor(136, 187, 255),
            new GbStroke(width.getWidth(zoomLevel), new float[]{12, 5}) // TODO
        );
    }
}
