package com.tschanz.geobooster.utfgrid_composer.service;

import com.tschanz.geobooster.map_layer.model.MapLayerType;
import com.tschanz.geobooster.map_layer.service.MapLayerService;
import com.tschanz.geobooster.utfgrid.model.UtfGrid;
import com.tschanz.geobooster.utfgrid.service.UtfGridRenderer;
import com.tschanz.geobooster.utfgrid_composer.model.*;
import com.tschanz.geobooster.util.service.ArrayHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class UtfGridComposer {
    private final static int MAX_POINT_ENTRIES = 5000;
    private final static int MAX_LINE_ENTRIES = 2500;

    private final MapLayerService mapLayerService;
    private final UtfGridHaltestelleConverter utfGridHstConverter;
    private final UtfGridTarifkanteConverter utfGridTkConverter;
    private final UtfGridVerkehrskanteConverter utfGridVkConverter;
    private final UtfGridHstWegangabeConverter utfGridHstWegangabeConverter;
    private final UtfGridRenderer utfGridRenderer;


    public UtfGridResponse getResponse(UtfGridRequest request) {
        var mapLayers = this.mapLayerService.searchObjects(request);
        var zoomLevel = request.getZoomLevel();
        var mapLayerStyles = request.getMapStyles();
        var isUnmappedTk = request.getMapLayerTypes().contains(MapLayerType.UNMAPPED_TARIFKANTE);

        var pointItems = Stream.concat(
            mapLayers.getHaltestelleVersions().stream().map(hst -> this.utfGridHstConverter.toUtfGrid(hst, zoomLevel, mapLayerStyles)),
            mapLayers.getHstWegangabeVersions().stream().map(hstWa -> this.utfGridHstWegangabeConverter.toUtfGrid(hstWa, zoomLevel, mapLayerStyles))
        ).collect(Collectors.toList());

        if (pointItems.size() > MAX_POINT_ENTRIES) {
            pointItems = ArrayHelper.removeRandomEntries(pointItems, (double) MAX_POINT_ENTRIES / pointItems.size());
        }

        var lineItems = Stream.concat(
            mapLayers.getTarifkanteVersions().stream().map(tkV -> this.utfGridTkConverter.toUtfGrid(tkV, zoomLevel, mapLayerStyles, isUnmappedTk)),
            mapLayers.getVerkehrskanteVersions().stream().map(vkV -> this.utfGridVkConverter.toUtfGrid(vkV, zoomLevel, mapLayerStyles))
        ).collect(Collectors.toList());

        if (lineItems.size() > MAX_LINE_ENTRIES) {
            lineItems = ArrayHelper.removeRandomEntries(lineItems, (double) MAX_LINE_ENTRIES / lineItems.size());
        }

        var utfGrid = new UtfGrid(
            request.getWidth(),
            request.getHeight(),
            request.getBbox().getMinCoordinate(),
            request.getBbox().getMaxCoordinate(),
            pointItems,
            lineItems
        );
        utfGrid.draw();
        var utfGridJson = this.utfGridRenderer.render(utfGrid);

        return new UtfGridResponse(utfGridJson);
    }
}
