package com.tschanz.geobooster.map_tile_composer.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class MapTileResponse {
    private final byte[] imgBytes;
}
