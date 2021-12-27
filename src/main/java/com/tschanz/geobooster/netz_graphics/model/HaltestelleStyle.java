package com.tschanz.geobooster.netz_graphics.model;

import com.tschanz.geobooster.graphics.model.GbColor;
import com.tschanz.geobooster.graphics.model.GbPointStyle;
import com.tschanz.geobooster.graphics.model.GbStroke;


public class HaltestelleStyle {
    private final static VariableWidth width = new VariableWidth(4, 13, 0, 13);


    public static GbPointStyle getStyle(float zoomLevel) {
        return new GbPointStyle(
            GbColor.GRAY,
            width.getWidth(zoomLevel),
            GbColor.BLACK,
            new GbStroke(1)
        );
    }
}
