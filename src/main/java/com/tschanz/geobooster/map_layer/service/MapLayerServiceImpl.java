package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.map_layer.model.MapLayerRequest;
import com.tschanz.geobooster.map_layer.model.MapLayerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
@RequiredArgsConstructor
public class MapLayerServiceImpl implements MapLayerService {
    private final HaltestelleLayerService haltestelleLayerService;
    private final VerkehrskanteLayerService verkehrskanteLayerService;
    private final TarifkanteLayerService tarifkanteLayerService;
    private final UnmappedTarifkanteLayerService unmappedTarifkanteLayerService;


    @Override
    public MapLayerResponse searchObjects(MapLayerRequest request) {
        var response = new MapLayerResponse(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        for (var layer: request.getMapLayers()) {
            switch (layer) {
                case HALTESTELLE:
                    var hstVersions = this.haltestelleLayerService.searchObjects(request);
                    response.getHaltestelleVersions().addAll(hstVersions);
                    break;
                case VERKEHRSKANTE:
                    var vkVersions = this.verkehrskanteLayerService.searchObjects(request);
                    response.getVerkehrskanteVersions().addAll(vkVersions);
                    break;
                case TARIFKANTE:
                    var tkVersions = this.tarifkanteLayerService.searchObjects(request);
                    response.getTarifkanteVersions().addAll(tkVersions);
                    break;
                case UNMAPPED_TARIFKANTE:
                    var unmTkVersions = this.unmappedTarifkanteLayerService.searchObjects(request);
                    response.getTarifkanteVersions().addAll(unmTkVersions);
                    break;
            }
        }

        return response;
    }
}
