package com.tschanz.geobooster.map_tile_composer.service;

import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.map_layer.service.MapLayerService;
import com.tschanz.geobooster.map_tile.model.MapTile;
import com.tschanz.geobooster.map_tile.service.MapTileRenderer;
import com.tschanz.geobooster.map_tile_composer.model.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class MapTileComposer {
    private final MapLayerService mapLayerService;
    private final MapTileHaltestelleConverter hstConverter;
    private final MapTileVerkehrskanteConverter vkConverter;
    private final MapTileTarifkanteConverter tkConverter;
    private final MapTileHstWegangabeConverter hstWaConverter;
    private final MapTileRenderer mapTileRenderer;


    @SneakyThrows
    public MapTileResponse getResponse(MapTileRequest request) {
        var mapObjects = this.mapLayerService.searchObjects(request);
        var zoomLevel = request.getZoomLevel();
        var mapLayerStyles = request.getMapStyles();

        // TMP: mem profiling
        /* System.out.println(GraphLayout.parseInstance(this.haltestelleRepo.getVersionedObjectMap()).toFootprint());
        System.out.println(GraphLayout.parseInstance(this.verkehrskanteRepo.getVersionedObjectMap()).toFootprint());
        System.out.println(GraphLayout.parseInstance(this.tarifkanteRepo.getVersionedObjectMap()).toFootprint()); */

        var mapTilePoints = Stream.concat(
            mapObjects.getHaltestelleVersions().stream().map(hstV -> this.hstConverter.toMapTile(hstV, zoomLevel, mapLayerStyles)),
            mapObjects.getHstWegangabeVersions().stream().map(hstWaV -> this.hstWaConverter.toMapTile(hstWaV, zoomLevel, mapLayerStyles))
        ).collect(Collectors.toList());

        var mapTileLines = Stream.concat(
            mapObjects.getTarifkanteVersions().stream().map(tkV -> this.tkConverter.toMapTile(tkV, zoomLevel, mapLayerStyles)),
            mapObjects.getVerkehrskanteVersions().stream().map(vkV -> this.vkConverter.toMapTile(vkV, zoomLevel, mapLayerStyles))
        ).collect(Collectors.toList());

        var tile = new MapTile(
            request.getWidth(),
            request.getHeight(),
            request.isBgTransparent(),
            CoordinateConverter.convertToEpsg3857(request.getBbox().getMinCoordinate()),
            CoordinateConverter.convertToEpsg3857(request.getBbox().getMaxCoordinate()),
            mapTilePoints,
            mapTileLines
        );
        var img = mapTileRenderer.render(tile);
        var bos = img.getBytes();

        return new MapTileResponse(bos);
    }
}
