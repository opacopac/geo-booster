package com.tschanz.geobooster.rtm.model;

import com.tschanz.geobooster.versioning.model.Element;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class RgAuspraegung implements Element {
    private final long id;
    private final long relationsgebietId;
}
