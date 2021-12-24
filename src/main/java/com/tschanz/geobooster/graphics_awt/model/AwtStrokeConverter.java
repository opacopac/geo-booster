package com.tschanz.geobooster.graphics_awt.model;

import com.tschanz.geobooster.graphics.model.GbStroke;

import java.awt.*;


public class AwtStrokeConverter {
    public static Stroke toAwt(GbStroke stroke) {
        return new BasicStroke(
            stroke.getWidth(),
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_BEVEL,
            0,
            stroke.getDash(),
            0
        );
    }
}
