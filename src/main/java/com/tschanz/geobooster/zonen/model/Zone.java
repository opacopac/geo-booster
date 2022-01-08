package com.tschanz.geobooster.zonen.model;

import com.tschanz.geobooster.versioning.model.Element;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class Zone implements Element {
    private final long id;
    private final long zonenplanId;
}
