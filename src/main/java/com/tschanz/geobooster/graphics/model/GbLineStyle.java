package com.tschanz.geobooster.graphics.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class GbLineStyle {
    private final GbColor color;
    private final GbStroke stroke;
}
