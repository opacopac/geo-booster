package com.tschanz.geobooster.graphics.model;

import lombok.Getter;

import java.awt.*;


@Getter
public class GbLineStyle {
    public static final GbLineStyle tmpBlackLine = new GbLineStyle(Color.BLACK, 3);
    public static final GbLineStyle tmpBlueLine = new GbLineStyle(Color.BLUE, 3);

    private final Color color;
    private final float width;
    private final Stroke stroke;


    public GbLineStyle(Color color, float width) {
        this.color = color;
        this.width = width;
        this.stroke = new BasicStroke(this.width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    }
}
