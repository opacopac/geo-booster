package com.tschanz.geobooster.utfgrid.model;

import com.tschanz.geobooster.util.model.KeyValue;

import java.util.List;


public interface UtfGridItem {
    List<KeyValue<String, ?>> getDataFields();
}
