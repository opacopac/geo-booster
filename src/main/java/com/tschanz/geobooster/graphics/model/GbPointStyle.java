package com.tschanz.geobooster.graphics.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class GbPointStyle {
    private final GbColor color;
    private final float width;
    private final GbColor borderColor;
    private final GbStroke borderStroke;
}
