package com.tschanz.geobooster.quadtree.model;

import com.tschanz.geobooster.geofeature.model.Coordinate;
import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.versioning.model.HasId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Getter
@RequiredArgsConstructor
public class QuadTreeNode<T extends HasId> {
    private final Extent<Epsg3857Coordinate> extent;
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


    public List<QuadTreeItem<T>> findItems(Extent<Epsg3857Coordinate> extent) {
        var ownItems = this.items.stream()
            .filter(item -> extent.isPointInside(item.getCoordinate()));

        var childItems = this.childNodes.stream()
            .filter(childNode -> childNode.getExtent().isExtentIntersecting(extent))
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

        var extent1 = Extent.fromEpsg3857Points(minX, minY, midX, midY);
        var extent2 = Extent.fromEpsg3857Points(midX, minY, maxX, midY);
        var extent3 = Extent.fromEpsg3857Points(minX, midY, midX, maxY);
        var extent4 = Extent.fromEpsg3857Points(midX, midY, maxX, maxY);

        this.childNodes.add(new QuadTreeNode<>(extent1, this, this.maxDepth));
        this.childNodes.add(new QuadTreeNode<>(extent2, this, this.maxDepth));
        this.childNodes.add(new QuadTreeNode<>(extent3, this, this.maxDepth));
        this.childNodes.add(new QuadTreeNode<>(extent4, this, this.maxDepth));
    }


    private QuadTreeNode<T> getChildForCoordinate(Coordinate coordinate) {
        return this.childNodes.stream()
            .filter(childNode -> childNode.extent.isPointInside(coordinate))
            .findFirst()
            .orElse(null);
    }
}
