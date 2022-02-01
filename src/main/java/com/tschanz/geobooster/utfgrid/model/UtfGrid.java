package com.tschanz.geobooster.utfgrid.model;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.util.model.Tuple2;
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
    private List<Tuple2<Integer, UtfGridItem>> numberedItems;
    private int imgWidth;
    private int imgHeight;


    public void draw() {
        this.imgWidth = this.width / REDUCTION_FACTOR;
        this.imgHeight = this.height / REDUCTION_FACTOR;
        this.utfGridImg = new UtfGridImg(this.imgWidth, this.imgHeight);

        for (var entry: this.getNumberedItems()) {
            var symbol = this.getSymbol(entry.getFirst());

            if (entry.getSecond() instanceof UtfGridLineItem) {
                var lineItem = (UtfGridLineItem) entry.getSecond();
                var width = lineItem.getWidth() / REDUCTION_FACTOR;
                var x0 = this.getX(lineItem.getStartCoordinate());
                var y0 = this.getY(lineItem.getStartCoordinate());
                var x1 = this.getX(lineItem.getEndCoordinate());
                var y1 = this.getY(lineItem.getEndCoordinate());
                this.utfGridImg.drawLine(x0, y0, x1, y1, width, symbol);
            }

            if (entry.getSecond() instanceof UtfGridPointItem) {
                var pointItem = (UtfGridPointItem) entry.getSecond();
                var width = pointItem.getWidth() / REDUCTION_FACTOR;
                var x = this.getX(pointItem.getCoordinate());
                var y = this.getY(pointItem.getCoordinate());
                this.utfGridImg.drawPoint(x, y, width, symbol);
            }
        }
    }


    public List<Tuple2<Integer, UtfGridItem>> getNumberedItems() {
        if (this.numberedItems == null) {
            this.numberedItems = this.calcNumberedItems();
        }

        return this.numberedItems;
    }


    private List<Tuple2<Integer, UtfGridItem>> calcNumberedItems() {
        var list = new ArrayList<Tuple2<Integer, UtfGridItem>>();
        for (var i = 0; i < lineItems.size(); i++) {
            list.add(new Tuple2<>(i + 1, lineItems.get(i)));
        }

        var offset = list.size() + 1;
        for (var i = 0; i < pointItems.size(); i++) {
            list.add(new Tuple2<>(i + offset, pointItems.get(i)));
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
