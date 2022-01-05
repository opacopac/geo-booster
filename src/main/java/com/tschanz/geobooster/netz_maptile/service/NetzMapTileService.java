package com.tschanz.geobooster.netz_maptile.service;

import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.maptile.model.MapTile;
import com.tschanz.geobooster.maptile.model.MapTileLine;
import com.tschanz.geobooster.maptile.model.MapTilePoint;
import com.tschanz.geobooster.maptile.service.MapTileService;
import com.tschanz.geobooster.netz_maptile.model.*;
import com.tschanz.geobooster.netz_repo.service.NetzSearchService;
import com.tschanz.geobooster.netz_repo.service.TarifkanteRepo;
import com.tschanz.geobooster.netz_repo.service.VerkehrskanteRepo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class NetzMapTileService {
    private final NetzSearchService netzSearchService;
    private final VerkehrskanteRepo verkehrskanteRepo;
    private final TarifkanteRepo tarifkanteRepo;
    private final MapTileService mapTileService;


    @SneakyThrows
    public NetzMapTileResponse getResponse(NetzMapTileRequest request) {
        var netzObjects = this.netzSearchService.searchNetzObjects(request);

        // TMP: mem profiling
        /* System.out.println(GraphLayout.parseInstance(this.haltestelleRepo.getVersionedObjectMap()).toFootprint());
        System.out.println(GraphLayout.parseInstance(this.verkehrskanteRepo.getVersionedObjectMap()).toFootprint());
        System.out.println(GraphLayout.parseInstance(this.tarifkanteRepo.getVersionedObjectMap()).toFootprint()); */

        var mapTilePoints = netzObjects.getHaltestelleVersions().stream()
            .map(hstV -> new MapTilePoint(
                CoordinateConverter.convertToEpsg3857(hstV.getCoordinate()),
                HaltestelleStyle.getStyle(request.getZoomLevel())
            ))
            .collect(Collectors.toList());

        var mapTileLines = Stream.concat(
            netzObjects.getTarifkanteVersions()
                .stream()
                .map(tkV -> new MapTileLine(
                    CoordinateConverter.convertToEpsg3857(this.tarifkanteRepo.getStartCoordinate(tkV)),
                    CoordinateConverter.convertToEpsg3857(this.tarifkanteRepo.getEndCoordinate(tkV)),
                    TarifkanteStyle.getStyle(request.getZoomLevel())
                )),
                netzObjects.getVerkehrskanteVersions()
                    .stream()
                    .map(vkV -> new MapTileLine(
                        CoordinateConverter.convertToEpsg3857(this.verkehrskanteRepo.getStartCoordinate(vkV)),
                        CoordinateConverter.convertToEpsg3857(this.verkehrskanteRepo.getEndCoordinate(vkV)),
                        VerkehrskanteStyle.getStyle(request.getZoomLevel())
                    ))
            )
            .collect(Collectors.toList());

        var tile = new MapTile(
            request.getWidth(),
            request.getHeight(),
            request.isBgTransparent(),
            CoordinateConverter.convertToEpsg3857(request.getBbox().getMinCoordinate()),
            CoordinateConverter.convertToEpsg3857(request.getBbox().getMaxCoordinate()),
            mapTilePoints,
            mapTileLines
        );
        var img = mapTileService.renderTile(tile);
        var bos = img.getBytes();

        return new NetzMapTileResponse(bos);
    }
}
