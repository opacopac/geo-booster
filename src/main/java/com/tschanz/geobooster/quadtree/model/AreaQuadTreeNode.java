package com.tschanz.geobooster.quadtree.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Getter
@RequiredArgsConstructor
public class AreaQuadTreeNode<T> {
    private final QuadTreeExtent extent;
    private final AreaQuadTreeNode<T> parentNode;
    private final int maxDepth;
    private final List<AreaQuadTreeNode<T>> childNodes = new ArrayList<>();
    private final List<AreaQuadTreeItem<T>> items = new ArrayList<>();


    public boolean isLeafNode() {
        return this.getNodeDepth() == this.maxDepth;
    }


    public int getNodeDepth() {
        if (this.parentNode == null) {
            return 0;
        } else {
            return this.parentNode.getNodeDepth() + 1;
        }
    }


    public List<AreaQuadTreeItem<T>> findItems(QuadTreeExtent extent) {
        var ownItems = this.items.stream()
            .filter(item -> extent.isIntersecting(item.getExtent()));

        var childItems = this.childNodes.stream()
            .filter(childNode -> childNode.getExtent().isIntersecting(extent))
            .flatMap(childNode -> childNode.findItems(extent).stream());

        return Stream.concat(ownItems, childItems).collect(Collectors.toList());
    }


    public boolean addItem(AreaQuadTreeItem<T> item) {
        if (this.isLeafNode()) {
            this.items.add(item);
        } else {
            if (this.childNodes.size() == 0) {
                this.createChildNodes();
            }

            var child = this.getChildForArea(item.getExtent());
            if (child != null) {
                child.addItem(item);
            } else {
                this.items.add(item);
            }
        }

        return true;
    }


    private void createChildNodes() {
        var minX = this.extent.getMinCoordinate().getX();
        var maxX = this.extent.getMaxCoordinate().getX();
        var midX = minX + (maxX - minX) / 2;

        var minY = this.extent.getMinCoordinate().getY();
        var maxY = this.extent.getMaxCoordinate().getY();
        var midY = minY + (maxY - minY) / 2;

        var extent1 = new QuadTreeExtent(new QuadTreeCoordinate(minX, minY), new QuadTreeCoordinate(midX, midY));
        var extent2 = new QuadTreeExtent(new QuadTreeCoordinate(midX, minY), new QuadTreeCoordinate(maxX, midY));
        var extent3 = new QuadTreeExtent(new QuadTreeCoordinate(minX, midY), new QuadTreeCoordinate(midX, maxY));
        var extent4 = new QuadTreeExtent(new QuadTreeCoordinate(midX, midY), new QuadTreeCoordinate(maxX, maxY));

        this.childNodes.add(new AreaQuadTreeNode<T>(extent1, this, this.maxDepth));
        this.childNodes.add(new AreaQuadTreeNode<T>(extent2, this, this.maxDepth));
        this.childNodes.add(new AreaQuadTreeNode<T>(extent3, this, this.maxDepth));
        this.childNodes.add(new AreaQuadTreeNode<T>(extent4, this, this.maxDepth));
    }


    private AreaQuadTreeNode<T> getChildForArea(QuadTreeExtent extent) {
        return this.childNodes.stream()
            .filter(childNode -> childNode.extent.isFullyInside(extent))
            .findFirst()
            .orElse(null);
    }
}