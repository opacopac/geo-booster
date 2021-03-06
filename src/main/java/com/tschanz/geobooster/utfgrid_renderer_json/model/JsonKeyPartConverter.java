package com.tschanz.geobooster.utfgrid_renderer_json.model;

import com.google.gson.JsonArray;
import com.tschanz.geobooster.utfgrid.model.UtfGrid;


public class JsonKeyPartConverter {
    public static JsonArray toJson(UtfGrid utfGrid) {
        var jsonKeys = new JsonArray();
        jsonKeys.add("");
        utfGrid.getNumberedItems().forEach(item -> jsonKeys.add(item.getFirst().toString()));

        return jsonKeys;
    }
}
