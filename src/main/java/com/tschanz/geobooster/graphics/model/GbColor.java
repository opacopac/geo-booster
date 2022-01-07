package com.tschanz.geobooster.graphics.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class GbColor {
    public final static GbColor BLACK = new GbColor(0, 0, 0);
    public final static GbColor GRAY = new GbColor(127, 127, 127);
    public final static GbColor WHITE = new GbColor(255, 255, 255);
    public final static GbColor BLUE = new GbColor(0, 0, 255);
    public final static GbColor GOLD = new GbColor(253, 183, 80);
    public final static GbColor LIGHT_BLUE = new GbColor(136, 187, 255);


    private final int r;
    private final int g;
    private final int b;
}
