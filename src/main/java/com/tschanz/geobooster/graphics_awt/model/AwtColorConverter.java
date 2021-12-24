package com.tschanz.geobooster.graphics_awt.model;

import com.tschanz.geobooster.graphics.model.GbColor;

import java.awt.*;


public class AwtColorConverter {
    public static Color toAwt(GbColor color) {
        return new Color(color.getR(), color.getG(), color.getB());
    }
}
