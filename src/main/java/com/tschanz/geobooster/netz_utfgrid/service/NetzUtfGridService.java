package com.tschanz.geobooster.netz_utfgrid.service;

import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.map_layer.model.MapLayer;
import com.tschanz.geobooster.map_layer.service.MapLayerService;
import com.tschanz.geobooster.netz_utfgrid.model.*;
import com.tschanz.geobooster.utfgrid.model.UtfGrid;
import com.tschanz.geobooster.utfgrid.service.UtfGridService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class NetzUtfGridService {
    private final MapLayerService mapLayerService;
    private final UtfGridService utfGridService;
    private final UtfGridHaltestelleConverter utfGridHstConverter;
    private final UtfGridTarifkanteConverter utfGridTkConverter;
    private final UtfGridVerkehrskanteConverter utfGridVkConverter;


    public NetzUtfGridResponse getResponse(NetzUtfGridRequest request) {
        var netzObjects = this.mapLayerService.searchObjects(request);

        var pointItems = netzObjects.getHaltestelleVersions().stream()
            .map(hst -> this.utfGridHstConverter.toUtfGrid(hst, request.getZoomLevel()))
            .collect(Collectors.toList());

        var lineItems = Stream.concat(
            netzObjects.getTarifkanteVersions().stream().map(tkV -> this.utfGridTkConverter.toUtfGrid(tkV, request.getZoomLevel(), request.getMapLayers().contains(MapLayer.UNMAPPED_TARIFKANTE))),
            netzObjects.getVerkehrskanteVersions().stream().map(vkV -> this.utfGridVkConverter.toUtfGrid(vkV, request.getZoomLevel()))
        ).collect(Collectors.toList());

        var utfGrid = new UtfGrid(
            request.getWidth(),
            request.getHeight(),
            CoordinateConverter.convertToEpsg3857(request.getBbox().getMinCoordinate()),
            CoordinateConverter.convertToEpsg3857(request.getBbox().getMaxCoordinate()),
            pointItems,
            lineItems
        );
        utfGrid.render();
        var utfGridJson = this.utfGridService.createJson(utfGrid);

        return new NetzUtfGridResponse(utfGridJson);
    }
}
