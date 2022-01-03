package com.tschanz.geobooster.quadtree.model;

import com.tschanz.geobooster.versioning.model.HasId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class AreaQuadTreeItem<T extends HasId> {
    private final QuadTreeExtent extent;
    private final T item;
}
