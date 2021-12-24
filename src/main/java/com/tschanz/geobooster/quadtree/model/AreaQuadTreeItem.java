package com.tschanz.geobooster.quadtree.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class AreaQuadTreeItem<T> {
    private final QuadTreeExtent extent;
    private final T item;
}
