package com.tschanz.geobooster.quadtree.model;

import com.tschanz.geobooster.versioning.model.HasId;
import lombok.Getter;

import java.util.List;


@Getter
public class QuadTree<T extends HasId> {
    private final int maxDepth;
    private final QuadTreeExtent extent;
    private final QuadTreeNode<T> rootNode;


    public QuadTree(
        int maxDepth,
        QuadTreeExtent extent
    ) {
        this.maxDepth = maxDepth;
        this.extent = extent;
        this.rootNode = new QuadTreeNode<T>(this.extent, null, this.maxDepth);
    }


    public List<QuadTreeItem<T>> findItems(QuadTreeExtent extent) {
        return this.rootNode.findItems(extent);
    }


    public boolean addItem(QuadTreeItem<T> item) {
        return this.rootNode.addItem(item);
    }
}
