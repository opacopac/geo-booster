package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.map_layer.model.MapLayerRequest;
import com.tschanz.geobooster.map_layer.model.MapLayerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MapLayerServiceImpl implements MapLayerService {
    private final HaltestelleLayerService haltestelleLayerService;
    private final VerkehrskanteLayerService verkehrskanteLayerService;
    private final TarifkanteLayerService tarifkanteLayerService;
    private final UnmappedTarifkanteLayerService unmappedTarifkanteLayerService;
    private final HaltestelleWegangabeLayerService haltestelleWegangabeLayerService;
    private final AwbVkLayerService awbVkLayerService;
    private final AwbTkLayerService awbTkLayerService;


    @Override
    public MapLayerResponse searchObjects(MapLayerRequest request) {
        var response = MapLayerResponse.createEmpty();

        for (var layer: request.getMapLayerTypes()) {
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
                case HALTESTELLE_WEGANGABE:
                    var hstWaVersions = this.haltestelleWegangabeLayerService.searchObjects(request);
                    response.getHstWegangabeVersions().addAll(hstWaVersions);
                    break;
                case ANWENDUNGSBEREICH_VK:
                    var awbVkVersions = this.awbVkLayerService.searchObjects(request);
                    response.getVerkehrskanteVersions().addAll(awbVkVersions);
                    break;
                case ANWENDUNGSBEREICH_TK:
                    var awbTkVersions = this.awbTkLayerService.searchObjects(request);
                    response.getTarifkanteVersions().addAll(awbTkVersions);
                    break;
            }
        }

        return response;
    }
}
