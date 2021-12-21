package com.tschanz.geobooster.webmapservice.service;

import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.graphics.model.GbLineStyle;
import com.tschanz.geobooster.graphics.model.GbPointStyle;
import com.tschanz.geobooster.graphics.service.ImageService;
import com.tschanz.geobooster.maptile.model.MapTile;
import com.tschanz.geobooster.maptile.model.MapTileLine;
import com.tschanz.geobooster.maptile.model.MapTilePoint;
import com.tschanz.geobooster.maptile.service.MapTileService;
import com.tschanz.geobooster.netz.service.HaltestellenRepo;
import com.tschanz.geobooster.netz.service.VerkehrskantenRepo;
import com.tschanz.geobooster.webmapservice.model.GetMapRequest;
import com.tschanz.geobooster.webmapservice.model.PngResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openjdk.jol.info.GraphLayout;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class WmsPngService {
    private static final Logger logger = LogManager.getLogger(WmsPngService.class);

    private final HaltestellenRepo haltestellenRepo;
    private final VerkehrskantenRepo verkehrskantenRepo;
    private final MapTileService mapTileService;
    private final ImageService imageService;


    @SneakyThrows
    public PngResponse getResponse(GetMapRequest mapRequest) {
        logger.info(String.format("request for bbox %s,%s %s,%s",
            CoordinateConverter.convertToEpsg4326(mapRequest.getBbox().getMinCoordinate()).getLatitude(),
            CoordinateConverter.convertToEpsg4326(mapRequest.getBbox().getMinCoordinate()).getLongitude(),
            CoordinateConverter.convertToEpsg4326(mapRequest.getBbox().getMaxCoordinate()).getLatitude(),
            CoordinateConverter.convertToEpsg4326(mapRequest.getBbox().getMaxCoordinate()).getLongitude()
        ));

        logger.info("reading haltestellen...");
        var hstVersions = this.haltestellenRepo.readHaltestellenVersions(LocalDate.now(), mapRequest.getBbox());
        logger.info("done.");

        logger.info("reading verkehrskanten...");
        var vkVersions = this.verkehrskantenRepo.readVerkehrskanteVersions(LocalDate.now(), mapRequest.getBbox());
        logger.info("done.");

        // TMP: mem profiling
        System.out.println(GraphLayout.parseInstance(this.haltestellenRepo).toFootprint());
        System.out.println(GraphLayout.parseInstance(this.verkehrskantenRepo).toFootprint());
        System.out.println(GraphLayout.parseInstance(this).toFootprint());

        logger.info("prepare map tile...");
        var mapTilePoints = hstVersions.stream()
            .map(hstV -> new MapTilePoint(
                CoordinateConverter.convertToEpsg3857(hstV.getCoordinate()),
                GbPointStyle.grayPointStyle
            ))
            .collect(Collectors.toList());

        var mapTileLines = vkVersions.stream()
            .map(vkV -> new MapTileLine(
                CoordinateConverter.convertToEpsg3857(vkV.getStartCoordinate()),
                CoordinateConverter.convertToEpsg3857(vkV.getEndCoordinate()),
                GbLineStyle.tmpBlackLine
            ))
            .collect(Collectors.toList());

        var tile = new MapTile(
            mapRequest.getWidth(),
            mapRequest.getHeight(),
            mapRequest.isTransparent(),
            CoordinateConverter.convertToEpsg3857(mapRequest.getBbox().getMinCoordinate()),
            CoordinateConverter.convertToEpsg3857(mapRequest.getBbox().getMaxCoordinate()),
            mapTilePoints,
            mapTileLines
        );
        logger.info("done.");

        logger.info("rendering tile...");
        var img = mapTileService.renderTile(tile);
        logger.info("done.");

        var bos = this.imageService.getPngByteStream(img);

        return new PngResponse(bos);
    }
}
