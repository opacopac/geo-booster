package com.tschanz.geobooster.webmapservice.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class WmsStats {
    private final int pngRequests;
    private final int utfGridRequests;
}
