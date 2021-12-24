package com.tschanz.geobooster.utfgrid_json.service;

import com.google.gson.GsonBuilder;
import com.tschanz.geobooster.utfgrid.model.UtfGrid;
import com.tschanz.geobooster.utfgrid.service.UtfGridService;
import com.tschanz.geobooster.utfgrid_json.model.JsonUtfGridConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class JsonUtfGridService implements UtfGridService {
    @Override
    public String render(UtfGrid utfGrid) {
        var utfGridJson = JsonUtfGridConverter.toJson(utfGrid);
        var gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(utfGridJson);
    }
}
