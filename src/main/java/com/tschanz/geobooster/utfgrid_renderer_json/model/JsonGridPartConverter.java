package com.tschanz.geobooster.utfgrid_renderer_json.model;

import com.google.gson.JsonArray;
import com.tschanz.geobooster.utfgrid.model.UtfGrid;


public class JsonGridPartConverter {
    public static JsonArray toJson(UtfGrid utfGrid) {
        var jsonGrid = new JsonArray();
        var bitMap = utfGrid.getUtfGridImg().getBitMap();

        for (char[] chars : bitMap) {
            jsonGrid.add(new String(chars));
            //jsonGrid.add("                                                                                                                                                                                                                                                                ");
        }

        return jsonGrid;
    }
}
