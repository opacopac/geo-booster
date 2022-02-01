package com.tschanz.geobooster.utfgrid_renderer_json.service;

import com.google.gson.GsonBuilder;
import com.tschanz.geobooster.utfgrid.model.UtfGrid;
import com.tschanz.geobooster.utfgrid.service.UtfGridRenderer;
import com.tschanz.geobooster.utfgrid_renderer_json.model.JsonUtfGridConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class JsonUtfGridRenderer implements UtfGridRenderer {
    @Override
    public String render(UtfGrid utfGrid) {
        var utfGridJson = JsonUtfGridConverter.toJson(utfGrid);
        var gson = new GsonBuilder()
            .serializeNulls()
            //.setPrettyPrinting()
            .create();

        return gson.toJson(utfGridJson);
    }
}
