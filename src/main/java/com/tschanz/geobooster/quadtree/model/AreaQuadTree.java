package com.tschanz.geobooster.quadtree.model;

import com.tschanz.geobooster.versioning.model.HasId;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
public class AreaQuadTree<T extends HasId> {
    private final int maxDepth;
    private final QuadTreeExtent extent;
    private final AreaQuadTreeNode<T> rootNode;
    private final Map<Long, AreaQuadTreeNode<T>> idLookupMap;


    public AreaQuadTree(
        int maxDepth,
        QuadTreeExtent extent
    ) {
        this.maxDepth = maxDepth;
        this.extent = extent;
        this.idLookupMap = new HashMap<>();
        this.rootNode = new AreaQuadTreeNode<T>(this.extent, null, this.maxDepth);
    }


    public List<AreaQuadTreeItem<T>> findItems(QuadTreeExtent extent) {
        return this.rootNode.findItems(extent);
    }


    public void addItem(AreaQuadTreeItem<T> item) {
        this.rootNode.addItem(item, this.idLookupMap);
    }


    public void removeItem(long itemId) {
        var node = this.idLookupMap.get(itemId);
        if (node != null) {
            node.removeItem(itemId);
        }
    }
}
