package com.tschanz.geobooster.netz_graphics.model;

import com.tschanz.geobooster.graphics.model.GbColor;
import com.tschanz.geobooster.graphics.model.GbLineStyle;
import com.tschanz.geobooster.graphics.model.GbPointStyle;
import com.tschanz.geobooster.graphics.model.GbStroke;


public class NetzStyles {
    public final static GbPointStyle HALTESTELLE_POINTSTYLE = new GbPointStyle(GbColor.GRAY, 5, GbColor.BLACK, new GbStroke(1));
    public static final GbLineStyle VERKEHRSKANTE_LINESTYLE = new GbLineStyle(GbColor.BLACK, new GbStroke(3));
    public static final GbLineStyle TARIFKANTE_LINESTYLE = new GbLineStyle(GbColor.BLUE, new GbStroke(3));
}
