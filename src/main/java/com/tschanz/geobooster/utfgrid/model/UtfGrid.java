package com.tschanz.geobooster.utfgrid.model;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.util.model.KeyValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
public class UtfGrid {
    private static final int REDUCTION_FACTOR = 4;

    @Getter private final int width;
    @Getter private final int height;
    @Getter private final Epsg3857Coordinate minCoordinate;
    @Getter private final Epsg3857Coordinate maxCoordinate;
    @Getter private final List<UtfGridPointItem> pointItems;
    @Getter private final List<UtfGridLineItem> lineItems;
    @Getter private UtfGridImg utfGridImg;
    private List<KeyValue<Integer, UtfGridItem>> numberedItems;
    private int imgWidth;
    private int imgHeight;


    public void render() {
        this.imgWidth = this.width / REDUCTION_FACTOR;
        this.imgHeight = this.height / REDUCTION_FACTOR;
        this.utfGridImg = new UtfGridImg(this.imgWidth, this.imgHeight);

        for (var entry: this.getNumberedItems()) {
            var symbol = this.getSymbol(entry.getKey());

            if (entry.getValue() instanceof UtfGridLineItem) {
                var lineItem = (UtfGridLineItem) entry.getValue();
                var width = lineItem.getWidth() / REDUCTION_FACTOR;
                var x0 = this.getX(lineItem.getStartCoordinate());
                var y0 = this.getY(lineItem.getStartCoordinate());
                var x1 = this.getX(lineItem.getEndCoordinate());
                var y1 = this.getY(lineItem.getEndCoordinate());
                this.utfGridImg.drawLine(x0, y0, x1, y1, width, symbol);
            }

            if (entry.getValue() instanceof UtfGridPointItem) {
                var pointItem = (UtfGridPointItem) entry.getValue();
                var width = pointItem.getWidth() / REDUCTION_FACTOR;
                var x = this.getX(pointItem.getCoordinate());
                var y = this.getY(pointItem.getCoordinate());
                this.utfGridImg.drawPoint(x, y, width, symbol);
            }
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
        for (var i = 0; i < lineItems.size(); i++) {
            list.add(new KeyValue<>(i + 1, lineItems.get(i)));
        }

        var offset = list.size() + 1;
        for (var i = 0; i < pointItems.size(); i++) {
            list.add(new KeyValue<>(i + offset, pointItems.get(i)));
        }

        return list;
    }


    private int getX(Epsg3857Coordinate coord) {
        var scale = this.imgWidth / (this.maxCoordinate.getE() - this.minCoordinate.getE());
        return (int) ((coord.getE() - this.minCoordinate.getE()) * scale);
    }


    private int getY(Epsg3857Coordinate coord) {
        var scale = this.imgHeight / (this.maxCoordinate.getN() - this.minCoordinate.getN());
        return (int) ((coord.getN() - this.minCoordinate.getN()) * scale);
    }


    // https://github.com/mapbox/mbtiles-spec/blob/master/1.1/utfgrid.md
    private char getSymbol(int itemIndex) {
        var charCode = itemIndex + 32;

        if (charCode >= 34) {
            charCode += 1;
        }

        if (charCode >= 92) {
            charCode += 1;
        }

        return (char) charCode;
    }
}
