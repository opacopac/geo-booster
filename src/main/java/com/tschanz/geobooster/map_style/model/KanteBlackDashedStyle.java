package com.tschanz.geobooster.map_style.model;

import com.tschanz.geobooster.graphics.model.GbColor;
import com.tschanz.geobooster.graphics.model.GbLineStyle;
import com.tschanz.geobooster.graphics.model.GbStroke;


public class KanteBlackDashedStyle {
    public final static ZoomVariableWidth WIDTH = new ZoomVariableWidth(4, 11, 0, 5);


    public static GbLineStyle getStyle(float zoomLevel) {
        return new GbLineStyle(
            GbColor.BLACK,
            new GbStroke(WIDTH.getWidth(zoomLevel), new float[]{12, 5})
        );
    }
}
