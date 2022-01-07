package com.tschanz.geobooster.rtm.model;

import com.tschanz.geobooster.versioning.model.Element;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class RgKorridor implements Element {
    private final long id;
    private final long relationsgbietId;
}
