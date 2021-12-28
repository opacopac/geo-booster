package com.tschanz.geobooster.webmapservice.service;

import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.maptile.model.MapTile;
import com.tschanz.geobooster.maptile.model.MapTileLine;
import com.tschanz.geobooster.maptile.model.MapTilePoint;
import com.tschanz.geobooster.maptile.service.MapTileService;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz.service.HaltestelleRepo;
import com.tschanz.geobooster.netz.service.TarifkanteRepo;
import com.tschanz.geobooster.netz.service.VerkehrskanteRepo;
import com.tschanz.geobooster.netz_graphics.model.HaltestelleStyle;
import com.tschanz.geobooster.netz_graphics.model.TarifkanteStyle;
import com.tschanz.geobooster.netz_graphics.model.VerkehrskanteStyle;
import com.tschanz.geobooster.webmapservice.model.GetMapRequest;
import com.tschanz.geobooster.webmapservice.model.PngResponse;
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
public class WmsPngService {
    private static final Logger logger = LogManager.getLogger(WmsPngService.class);

    private final HaltestelleRepo haltestelleRepo;
    private final VerkehrskanteRepo verkehrskanteRepo;
    private final TarifkanteRepo tarifkanteRepo;
    private final MapTileService mapTileService;


    @SneakyThrows
    public PngResponse getResponse(GetMapRequest mapRequest) {
        var date = mapRequest.getViewparams().getDate();
        var bbox = mapRequest.getBbox();
        var vmTypes = mapRequest.getViewparams().getTypes();

        logger.info("searching hst versions...");
        List<HaltestelleVersion> hstVersions = mapRequest.hasLayerHaltestellen()
            ? this.haltestelleRepo.readVersions(date, bbox)
            : Collections.emptyList();
        logger.info(String.format("found %d hst versions", hstVersions.size()));

        logger.info("searching vk versions...");
        List<VerkehrskanteVersion> vkVersions = mapRequest.hasLayerVerkehrskanten()
            ? this.verkehrskanteRepo.readVersions(date, bbox, vmTypes)
            : Collections.emptyList();
        logger.info(String.format("found %d vk versions", vkVersions.size()));

        logger.info("searching tk versions...");
        List<TarifkanteVersion> tkVersions = mapRequest.hasLayerTarifkanten()
            ? this.tarifkanteRepo.readVersions(date, bbox, vmTypes)
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
                HaltestelleStyle.getStyle(mapRequest.getZoomLevel())
            ))
            .collect(Collectors.toList());

        var mapTileLines = Stream.concat(
            tkVersions
                .stream()
                .map(tkV -> new MapTileLine(
                    CoordinateConverter.convertToEpsg3857(this.tarifkanteRepo.getStartCoordinate(tkV)),
                    CoordinateConverter.convertToEpsg3857(this.tarifkanteRepo.getEndCoordinate(tkV)),
                    TarifkanteStyle.getStyle(mapRequest.getZoomLevel())
                )),
                vkVersions
                    .stream()
                    .map(vkV -> new MapTileLine(
                        CoordinateConverter.convertToEpsg3857(this.verkehrskanteRepo.getStartCoordinate(vkV)),
                        CoordinateConverter.convertToEpsg3857(this.verkehrskanteRepo.getEndCoordinate(vkV)),
                        VerkehrskanteStyle.getStyle(mapRequest.getZoomLevel())
                    ))
            )
            .collect(Collectors.toList());

        var tile = new MapTile(
            mapRequest.getWidth(),
            mapRequest.getHeight(),
            mapRequest.isTransparent(),
            CoordinateConverter.convertToEpsg3857(bbox.getMinCoordinate()),
            CoordinateConverter.convertToEpsg3857(bbox.getMaxCoordinate()),
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

        return new PngResponse(bos);
    }
}
