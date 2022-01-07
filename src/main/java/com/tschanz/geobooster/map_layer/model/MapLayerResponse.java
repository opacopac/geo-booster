package com.tschanz.geobooster.map_layer.model;

import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabeVersion;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;


@Getter
@RequiredArgsConstructor
public class MapLayerResponse {
    private final Collection<HaltestelleVersion> haltestelleVersions;
    private final Collection<VerkehrskanteVersion> verkehrskanteVersions;
    private final Collection<TarifkanteVersion> tarifkanteVersions;
    private final Collection<HaltestelleWegangabeVersion> hstWegangabeVersions;


    public static MapLayerResponse createEmpty() {
        return new MapLayerResponse(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }
}
