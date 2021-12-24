package com.tschanz.geobooster.utfgrid.model;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.util.model.KeyValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
public class UtfGrid {
    @Getter private final Epsg3857Coordinate minCoordinate;
    @Getter private final Epsg3857Coordinate maxCoordinate;
    @Getter private final List<UtfGridPointItem> pointItems;
    @Getter private final List<UtfGridLineItem> lineItems;
    private List<KeyValue<Integer, UtfGridItem>> numberedItems;


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
}
