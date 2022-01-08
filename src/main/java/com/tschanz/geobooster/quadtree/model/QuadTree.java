package com.tschanz.geobooster.quadtree.model;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.versioning.model.HasId;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;


@Getter
public class QuadTree<T extends HasId> {
    private final int maxDepth;
    private QuadTreeNode<T> rootNode;
    private Extent<Epsg3857Coordinate> extent;


    public QuadTree(int maxDepth) {
        this.maxDepth = maxDepth;
    }


    public List<QuadTreeItem<T>> findItems(Extent<Epsg3857Coordinate> extent) {
        return this.rootNode.findItems(extent);
    }


    public void build(Collection<T> items, Function<T, Epsg3857Coordinate> getCoordFn) {
        var minX = 0.0; var minY = 0.0; var maxX = 0.0; var maxY = 0.0;
        var qtItems = new ArrayList<QuadTreeItem<T>>();
        for (var item: items) {
            var coord = getCoordFn.apply(item);
            if (coord != null) {
                minX = Math.min(minX, coord.getX());
                minY = Math.min(minY, coord.getY());
                maxX = Math.max(maxX, coord.getX());
                maxY = Math.max(maxY, coord.getY());
                qtItems.add(new QuadTreeItem<>(coord, item));
            }
        };

        this.extent = Extent.fromEpsg3857Points (minX, minY, maxX, maxY);
        this.rootNode = new QuadTreeNode<T>(this.extent, null, this.maxDepth);

        qtItems.forEach(qtItem -> this.rootNode.addItem(qtItem));
    }
}
