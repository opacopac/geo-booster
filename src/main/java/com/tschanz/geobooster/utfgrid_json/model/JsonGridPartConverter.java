package com.tschanz.geobooster.utfgrid_json.model;

import com.google.gson.JsonArray;
import com.tschanz.geobooster.utfgrid.model.UtfGrid;


public class JsonGridPartConverter {
    public static JsonArray toJson(UtfGrid utfGrid) {
        var jsonGrid = new JsonArray();

        for (var i = 0; i < 256; i++) {
            jsonGrid.add("                                                                                                                                                                                                                                                                ");
        }

        return jsonGrid;
    }
}
