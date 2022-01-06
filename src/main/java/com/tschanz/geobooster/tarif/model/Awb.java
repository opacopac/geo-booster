package com.tschanz.geobooster.tarif.model;

import com.tschanz.geobooster.versioning.model.Element;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class Awb implements Element {
    private final long id;
}
