package com.tschanz.geobooster.netz_graphics.model;

import com.tschanz.geobooster.graphics.model.GbColor;
import com.tschanz.geobooster.graphics.model.GbPointStyle;
import com.tschanz.geobooster.graphics.model.GbStroke;


public class HaltestelleStyle {
    public final static ZoomVariableWidth WIDTH = new ZoomVariableWidth(4, 13, 0, 13);


    public static GbPointStyle getStyle(float zoomLevel) {
        return new GbPointStyle(
            GbColor.GRAY,
            WIDTH.getWidth(zoomLevel),
            GbColor.BLACK,
            new GbStroke(1)
        );
    }
}
