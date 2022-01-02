package com.tschanz.geobooster.netz_maptile.service;

import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.maptile.model.MapTile;
import com.tschanz.geobooster.maptile.model.MapTileLine;
import com.tschanz.geobooster.maptile.model.MapTilePoint;
import com.tschanz.geobooster.maptile.service.MapTileService;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz_maptile.model.*;
import com.tschanz.geobooster.netz_repo.service.HaltestelleRepo;
import com.tschanz.geobooster.netz_repo.service.TarifkanteRepo;
import com.tschanz.geobooster.netz_repo.service.VerkehrskanteRepo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class NetzMapTileService {
    private static final Logger logger = LogManager.getLogger(NetzMapTileService.class);

    private final HaltestelleRepo haltestelleRepo;
    private final VerkehrskanteRepo verkehrskanteRepo;
    private final TarifkanteRepo tarifkanteRepo;
    private final MapTileService mapTileService;


    @SneakyThrows
    public NetzMapTileResponse getResponse(NetzMapTileRequest request) {
        logger.info("searching hst versions...");
        List<HaltestelleVersion> hstVersions = request.isShowHaltestellen()
            ? this.haltestelleRepo.searchVersions(request.getDate(), request.getBbox())
            : Collections.emptyList();
        logger.info(String.format("found %d hst versions", hstVersions.size()));

        logger.info("searching vk versions...");
        List<VerkehrskanteVersion> vkVersions = request.isShowVerkehrskanten()
            ? this.verkehrskanteRepo.searchVersions(request.getDate(), request.getBbox(), request.getVmTypes(), request.getVerwaltungVersionIds(), request.isShowTerminiert())
            : Collections.emptyList();
        logger.info(String.format("found %d vk versions", vkVersions.size()));

        logger.info("searching tk versions...");
        List<TarifkanteVersion> tkVersions = request.isShowTarifkanten()
            ? this.tarifkanteRepo.searchVersions(request.getDate(), request.getBbox(), request.getVmTypes(), request.getVerwaltungVersionIds())
            : Collections.emptyList();
        logger.info(String.format("found %d tk versions", tkVersions.size()));


        // TMP: mem profiling
        /* System.out.println(GraphLayout.parseInstance(this.haltestelleRepo.getVersionedObjectMap()).toFootprint());
        System.out.println(GraphLayout.parseInstance(this.verkehrskanteRepo.getVersionedObjectMap()).toFootprint());
        System.out.println(GraphLayout.parseInstance(this.tarifkanteRepo.getVersionedObjectMap()).toFootprint()); */


        logger.info("prepare map tile...");
        var mapTilePoints = hstVersions.stream()
            .map(hstV -> new MapTilePoint(
                CoordinateConverter.convertToEpsg3857(hstV.getCoordinate()),
                HaltestelleStyle.getStyle(request.getZoomLevel())
            ))
            .collect(Collectors.toList());

        var mapTileLines = Stream.concat(
            tkVersions
                .stream()
                .map(tkV -> new MapTileLine(
                    CoordinateConverter.convertToEpsg3857(this.tarifkanteRepo.getStartCoordinate(tkV)),
                    CoordinateConverter.convertToEpsg3857(this.tarifkanteRepo.getEndCoordinate(tkV)),
                    TarifkanteStyle.getStyle(request.getZoomLevel())
                )),
                vkVersions
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
        logger.info("done.");

        logger.info("rendering tile...");
        var img = mapTileService.renderTile(tile);
        logger.info("done.");

        logger.info("getting image bytes...");
        var bos = img.getBytes();
        logger.info("done.");

        return new NetzMapTileResponse(bos);
    }
}
