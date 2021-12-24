package com.tschanz.geobooster.graphics.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class GbLineStyle {
    public static final GbLineStyle tmpBlackLine = new GbLineStyle(GbColor.BLACK, new GbStroke(3));
    public static final GbLineStyle tmpBlueLine = new GbLineStyle(GbColor.BLUE, new GbStroke(3));

    private final GbColor color;
    private final GbStroke stroke;
}
