package com.tschanz.geobooster.netz_maptile.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class NetzMapTileResponse {
    private final byte[] imgBytes;
}
