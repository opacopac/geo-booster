package com.tschanz.geobooster.utfgrid.model;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.util.model.KeyValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
public class UtfGrid {
    private static final int REDUCTION_FACTOR = 2;

    @Getter private final int width;
    @Getter private final int height;
    @Getter private final Epsg3857Coordinate minCoordinate;
    @Getter private final Epsg3857Coordinate maxCoordinate;
    @Getter private final List<UtfGridPointItem> pointItems;
    @Getter private final List<UtfGridLineItem> lineItems;
    @Getter private UtfGridImg utfGridImg;
    private List<KeyValue<Integer, UtfGridItem>> numberedItems;


    public void render() {
        this.utfGridImg = new UtfGridImg(width / REDUCTION_FACTOR, height / REDUCTION_FACTOR, minCoordinate, maxCoordinate);

        for (var pointItem: this.pointItems) {
            var x = this.getX(pointItem.getCoordinate());
            var y = this.getY(pointItem.getCoordinate());
            this.utfGridImg.drawPoint(x, y, 'X'); // TODO
        }

        for (var lineItem: this.lineItems) {
            var x0 = this.getX(lineItem.getStartCoordinate());
            var y0 = this.getY(lineItem.getStartCoordinate());
            var x1 = this.getX(lineItem.getEndCoordinate());
            var y1 = this.getY(lineItem.getEndCoordinate());
            this.utfGridImg.drawLine(x0, y0, x1, y1, '*'); // TODO
        }
    }


    public List<KeyValue<Integer, UtfGridItem>> getNumberedItems() {
        if (this.numberedItems == null) {
            this.numberedItems = this.calcNumberedItems();
        }

        return this.numberedItems;
    }


    private List<KeyValue<Integer, UtfGridItem>> calcNumberedItems() {
        var list = new ArrayList<KeyValue<Integer, UtfGridItem>>();
        for (var i = 0; i < pointItems.size(); i++) {
            list.add(new KeyValue<>(i + 1, pointItems.get(i)));
        }

        var offset = list.size() + 1;
        for (var i = 0; i < lineItems.size(); i++) {
            list.add(new KeyValue<>(i + offset, lineItems.get(i)));
        }

        return list;
    }


    private int getX(Epsg3857Coordinate coord) {
        return (int) (coord.getE() - this.minCoordinate.getE()) / (this.width / REDUCTION_FACTOR);
    }


    private int getY(Epsg3857Coordinate coord) {
        return (int) (coord.getN() - this.minCoordinate.getN()) / (this.height / REDUCTION_FACTOR);
    }
}
