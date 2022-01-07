package com.tschanz.geobooster.utfgrid_renderer_json.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.tschanz.geobooster.utfgrid.model.UtfGrid;
import com.tschanz.geobooster.utfgrid.model.UtfGridItem;

import java.time.LocalDate;


public class JsonDataPartConverter {
    public static JsonObject toJson(UtfGrid utfGrid) {
        var jsonDataPart = new JsonObject();
        utfGrid.getNumberedItems().forEach(entry -> {
            jsonDataPart.add(entry.getKey().toString(), itemToJson(entry.getValue()));
        });

        return jsonDataPart;
    }


    private static JsonObject itemToJson(UtfGridItem item) {
        var jsonItem = new JsonObject();
        item.getDataFields().forEach(field -> {
            jsonItem.add(field.getKey(), valueToJson(field.getValue()));
        });

        return jsonItem;
    }


    private static JsonElement valueToJson(Object value) {
        if (value == null) {
            return JsonNull.INSTANCE;
        }

        if (value instanceof Number) {
            return new JsonPrimitive((Number) value);
        }

        if (value instanceof LocalDate) {
            return new JsonPrimitive(value.toString() + "T00:00:00Z"); // TODO
        }

        return new JsonPrimitive(value.toString());
    }
}
