package com.tschanz.geobooster.utfgrid_composer.service;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.map_layer.model.MapLayerType;
import com.tschanz.geobooster.map_layer.service.MapLayerService;
import com.tschanz.geobooster.utfgrid.model.UtfGrid;
import com.tschanz.geobooster.utfgrid.model.UtfGridLineItem;
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
    private final MapLayerService mapLayerService;
    private final UtfGridProperties utfGridProperties;
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

        if (pointItems.size() > this.utfGridProperties.getMaxPointEntries()) {
            pointItems = ArrayHelper.removeRandomEntries(pointItems, (double) this.utfGridProperties.getMaxPointEntries() / pointItems.size());
        }

        var minLineLength = this.getMinLineLength(request.getBbox(), request.getWidth());
        var lineItems = Stream.concat(
            mapLayers.getTarifkanteVersions().stream().map(tkV -> this.utfGridTkConverter.toUtfGrid(tkV, zoomLevel, mapLayerStyles, isUnmappedTk)),
            mapLayers.getVerkehrskanteVersions().stream().map(vkV -> this.utfGridVkConverter.toUtfGrid(vkV, zoomLevel, mapLayerStyles))
        )
            .filter(lineItem -> minLineLength == 0 || this.hasMinLength(lineItem, minLineLength))
            .collect(Collectors.toList());

        if (lineItems.size() > this.utfGridProperties.getMaxLineEntries()) {
            lineItems = ArrayHelper.removeRandomEntries(lineItems, (double) this.utfGridProperties.getMaxLineEntries() / lineItems.size());
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



    private double getMinLineLength(Extent<Epsg3857Coordinate> bbox, int widthPixel) {
        return (bbox.getMaxCoordinate().getE() - bbox.getMinCoordinate().getE()) / widthPixel * UtfGrid.REDUCTION_FACTOR * this.utfGridProperties.getLineMinLengthChars();
    }


    private boolean hasMinLength(UtfGridLineItem utfGridLine, double minLength) {
        var lenE = Math.abs(utfGridLine.getEndCoordinate().getE() - utfGridLine.getStartCoordinate().getE());
        if (lenE >= minLength) {
            return true;
        }

        var lenN = Math.abs(utfGridLine.getEndCoordinate().getN() - utfGridLine.getStartCoordinate().getN());
        return lenN >= minLength;
    }
}
