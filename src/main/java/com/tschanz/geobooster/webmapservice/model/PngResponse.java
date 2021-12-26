package com.tschanz.geobooster.webmapservice.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class PngResponse {
    private final byte[] imgBytes;
}
