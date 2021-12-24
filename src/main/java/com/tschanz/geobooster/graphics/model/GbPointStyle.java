package com.tschanz.geobooster.graphics.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class GbPointStyle {
    public final static GbPointStyle grayPointStyle = new GbPointStyle(GbColor.GRAY, 5, GbColor.BLACK, new GbStroke(1));

    private final GbColor color;
    private final int radius;
    private final GbColor borderColor;
    private final GbStroke borderStroke;
}
