package com.tschanz.geobooster.utfgrid.model;

import com.tschanz.geobooster.util.model.Tuple2;

import java.util.List;


public interface UtfGridItem {
    List<Tuple2<String, ?>> getDataFields();
}
