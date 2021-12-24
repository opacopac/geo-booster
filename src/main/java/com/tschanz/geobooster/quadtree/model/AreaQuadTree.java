package com.tschanz.geobooster.quadtree.model;

import lombok.Getter;

import java.util.List;


@Getter
public class AreaQuadTree<T> {
    private final int maxDepth;
    private final QuadTreeExtent extent;
    private final AreaQuadTreeNode<T> rootNode;


    public AreaQuadTree(
        int maxDepth,
        QuadTreeExtent extent
    ) {
        this.maxDepth = maxDepth;
        this.extent = extent;
        this.rootNode = new AreaQuadTreeNode<T>(this.extent, null, this.maxDepth);
    }


    public List<AreaQuadTreeItem<T>> findItems(QuadTreeExtent extent) {
        return this.rootNode.findItems(extent);
    }


    public boolean addItem(AreaQuadTreeItem<T> item) {
        return this.rootNode.addItem(item);
    }
}
