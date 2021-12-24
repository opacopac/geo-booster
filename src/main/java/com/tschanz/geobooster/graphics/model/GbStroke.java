package com.tschanz.geobooster.graphics.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class GbStroke {
    private final float width;
    private final float[] dash;


    public GbStroke(float width) {
        this.width = width;
        this.dash = null;
    }
}
