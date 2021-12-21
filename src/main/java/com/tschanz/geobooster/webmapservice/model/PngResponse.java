package com.tschanz.geobooster.webmapservice.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayOutputStream;


@Getter
@RequiredArgsConstructor
public class PngResponse {
    private final ByteArrayOutputStream imgByteStream;
}
