package com.tschanz.geobooster.map_style.model;

import com.tschanz.geobooster.graphics.model.GbColor;
import com.tschanz.geobooster.graphics.model.GbPointStyle;
import com.tschanz.geobooster.graphics.model.GbStroke;


public class WaHaltestelleStyle {
    public final static ZoomVariableWidth WIDTH = new ZoomVariableWidth(6, 13, 0, 13);


    public static GbPointStyle getStyle(float zoomLevel) {
        return new GbPointStyle(
            GbColor.GOLD,
            WIDTH.getWidth(zoomLevel),
            GbColor.BLACK,
            new GbStroke(1)
        );
    }
}
