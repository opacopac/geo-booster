package com.tschanz.geobooster.quadtree.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class QuadTreeItem<T> {
    private final QuadTreeCoordinate coordinate;
    private final T item;
}
