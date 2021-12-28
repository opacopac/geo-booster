package com.tschanz.geobooster.netz.model;

import com.tschanz.geobooster.versioning.model.Element;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class Haltestelle implements Element {
    private final long id;
    private final int uicCode;
}
