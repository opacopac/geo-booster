package com.tschanz.geobooster.utfgrid_json.model;

import com.google.gson.JsonObject;
import com.tschanz.geobooster.utfgrid.model.UtfGrid;


public class JsonUtfGridConverter {
    public static JsonObject toJson(UtfGrid utfGrid) {
        var jsonGrid = new JsonObject();
        jsonGrid.add("grid", JsonGridPartConverter.toJson(utfGrid));
        jsonGrid.add("keys", JsonKeyPartConverter.toJson(utfGrid));
        jsonGrid.add("data", JsonDataPartConverter.toJson(utfGrid));

        return jsonGrid;
    }
}
