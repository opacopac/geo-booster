package com.tschanz.geobooster.quadtree.model;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.versioning.model.HasId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class QuadTreeItem<T extends HasId> {
    private final Epsg3857Coordinate coordinate;
    private final T item;
}
