package com.tschanz.geobooster.quadtree.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Getter
@RequiredArgsConstructor
public class QuadTreeNode<T> {
    private final QuadTreeExtent extent;
    private final QuadTreeNode<T> parentNode;
    private final int maxDepth;
    private final List<QuadTreeNode<T>> childNodes = new ArrayList<>();
    private final List<QuadTreeItem<T>> items = new ArrayList<>();


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


    public List<QuadTreeItem<T>> findItems(QuadTreeExtent extent) {
        var ownItems = this.items.stream()
            .filter(item -> extent.isCoordinateInside(item.getCoordinate()));

        var childItems = this.childNodes.stream()
            .filter(childNode -> childNode.getExtent().isIntersecting(extent))
            .flatMap(childNode -> childNode.findItems(extent).stream());

        return Stream.concat(ownItems, childItems).collect(Collectors.toList());
    }


    public boolean addItem(QuadTreeItem<T> item) {
        if (this.isLeafNode()) {
            items.add(item);
        } else {
            if (this.childNodes.size() == 0) {
                this.createChildNodes();
            }

            var child = this.getChildForCoordinate(item.getCoordinate());
            if (child != null) {
                child.addItem(item);
            } else {
                return false;
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

        this.childNodes.add(new QuadTreeNode<T>(extent1, this, this.maxDepth));
        this.childNodes.add(new QuadTreeNode<T>(extent2, this, this.maxDepth));
        this.childNodes.add(new QuadTreeNode<T>(extent3, this, this.maxDepth));
        this.childNodes.add(new QuadTreeNode<T>(extent4, this, this.maxDepth));
    }


    private QuadTreeNode<T> getChildForCoordinate(QuadTreeCoordinate coordinate) {
        return this.childNodes.stream()
            .filter(childNode -> childNode.extent.isCoordinateInside(coordinate))
            .findFirst()
            .orElse(null);
    }
}
