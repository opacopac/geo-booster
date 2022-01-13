package com.tschanz.geobooster.versioning.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Getter
@RequiredArgsConstructor
public class VersionedObject<E extends Element, V extends Version>  {
    private final E element;
    private final List<V> versions;
}
