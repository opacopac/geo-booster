package com.tschanz.geobooster.versioning.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum Pflegestatus {
    IN_ARBEIT((byte) 1),
    TEST((byte) 2),
    ABNAHME((byte) 3),
    PRODUKTIV((byte) 4);

    private final byte sortOrder;
}
