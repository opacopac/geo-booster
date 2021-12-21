package com.tschanz.geobooster.graphics.model;

import lombok.Getter;

import java.awt.*;


@Getter
public class GbPointStyle {
    public final static GbPointStyle grayPointStyle = new GbPointStyle(Color.GRAY, 5, Color.BLACK);

    private final Color color;
    private final int radius;
    private final Color borderColor;
    private final Stroke stroke;


    public GbPointStyle(Color color, int radius, Color borderColor) {
        this.color = color;
        this.radius = radius;
        this.borderColor = borderColor;
        this.stroke = new BasicStroke(1);
    }
}
