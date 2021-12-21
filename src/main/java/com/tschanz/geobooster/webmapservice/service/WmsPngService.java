package com.tschanz.geobooster.webmapservice.service;

import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.graphics.model.GbLineStyle;
import com.tschanz.geobooster.graphics.model.GbPointStyle;
import com.tschanz.geobooster.graphics.service.ImageService;
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
    private static final String LAYER_HALTESTELLEN = "novap:HALTESTELLEN";
    private static final String LAYER_VERKEHRSKANTEN = "novap:VERKEHRSKANTEN";
    private static final String LAYER_TARIFKANTEN = "novap:TARIFKANTEN";
    private static final Logger logger = LogManager.getLogger(WmsPngService.class);

    private final HaltestelleRepo haltestelleRepo;
    private final VerkehrskanteRepo verkehrskanteRepo;
    private final TarifkanteRepo tarifkanteRepo;
    private final MapTileService mapTileService;
    private final ImageService imageService;


    @SneakyThrows
    public PngResponse getResponse(GetMapRequest mapRequest) {
        var date = mapRequest.getViewparams().getDate();
        var bbox = mapRequest.getBbox();

        logger.info(String.format("request for bbox %s,%s %s,%s",
            CoordinateConverter.convertToEpsg4326(bbox.getMinCoordinate()).getLatitude(),
            CoordinateConverter.convertToEpsg4326(bbox.getMinCoordinate()).getLongitude(),
            CoordinateConverter.convertToEpsg4326(bbox.getMaxCoordinate()).getLatitude(),
            CoordinateConverter.convertToEpsg4326(bbox.getMaxCoordinate()).getLongitude()
        ));

        List<HaltestelleVersion> hstVersions = Collections.emptyList();
        if (mapRequest.getLayers().contains(LAYER_HALTESTELLEN)) {
            logger.info("reading haltestellen...");
            hstVersions = this.haltestelleRepo.readHaltestellenVersions(date, bbox);
            logger.info("done.");
        }

        List<VerkehrskanteVersion> vkVersions = Collections.emptyList();
        if (mapRequest.getLayers().contains(LAYER_VERKEHRSKANTEN)) {
            logger.info("reading verkehrskanten...");
            vkVersions = this.verkehrskanteRepo.readVerkehrskanteVersions(date, bbox);
            logger.info("done.");
        }

        List<TarifkanteVersion> tkVersions = Collections.emptyList();
        if (mapRequest.getLayers().contains(LAYER_TARIFKANTEN)) {
            logger.info("reading tarifkanten...");
            tkVersions = this.tarifkanteRepo.readTarifkanteVersions(date, bbox);
            logger.info("done.");
        }

        // TMP: mem profiling
        /*System.out.println(GraphLayout.parseInstance(this.haltestelleRepo).toFootprint());
        System.out.println(GraphLayout.parseInstance(this.verkehrskanteRepo).toFootprint());
        System.out.println(GraphLayout.parseInstance(this.tarifkanteRepo).toFootprint());
        System.out.println(GraphLayout.parseInstance(this).toFootprint());*/

        logger.info("prepare map tile...");
        var mapTilePoints = hstVersions.stream()
            .map(hstV -> new MapTilePoint(
                CoordinateConverter.convertToEpsg3857(hstV.getCoordinate()),
                GbPointStyle.grayPointStyle
            ))
            .collect(Collectors.toList());

        var mapTileLines = Stream.concat(
                vkVersions.stream()
                    .map(vkV -> new MapTileLine(
                        CoordinateConverter.convertToEpsg3857(vkV.getStartCoordinate()),
                        CoordinateConverter.convertToEpsg3857(vkV.getEndCoordinate()),
                        GbLineStyle.tmpBlackLine
                    )),
                tkVersions.stream()
                    .map(tkV -> new MapTileLine(
                        CoordinateConverter.convertToEpsg3857(tkV.getStartCoordinate()),
                        CoordinateConverter.convertToEpsg3857(tkV.getEndCoordinate()),
                        GbLineStyle.tmpBlueLine
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

        var bos = this.imageService.getPngByteStream(img);

        return new PngResponse(bos);
    }
}
