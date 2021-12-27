package com.tschanz.geobooster.webmapservice.service;

import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz.service.HaltestelleRepo;
import com.tschanz.geobooster.netz.service.TarifkanteRepo;
import com.tschanz.geobooster.netz.service.VerkehrskanteRepo;
import com.tschanz.geobooster.netz_utfgrid.model.UtfGridHaltestelleConverter;
import com.tschanz.geobooster.netz_utfgrid.model.UtfGridTarifkanteConverter;
import com.tschanz.geobooster.netz_utfgrid.model.UtfGridVerkehrskanteConverter;
import com.tschanz.geobooster.utfgrid.model.UtfGrid;
import com.tschanz.geobooster.utfgrid.service.UtfGridService;
import com.tschanz.geobooster.webmapservice.model.GetMapRequest;
import com.tschanz.geobooster.webmapservice.model.UtfGridResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class WmsUtfGridService {
    private static final Logger logger = LogManager.getLogger(WmsUtfGridService.class);

    private final HaltestelleRepo haltestelleRepo;
    private final VerkehrskanteRepo verkehrskanteRepo;
    private final TarifkanteRepo tarifkanteRepo;
    private final UtfGridService utfGridService;


    public UtfGridResponse getResponse(GetMapRequest mapRequest) {
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

        logger.info("initializing utf grid...");
        var pointItems = hstVersions.stream()
            .map(hst -> UtfGridHaltestelleConverter.toUtfGrid(hst, mapRequest.getZoomLevel()))
            .collect(Collectors.toList());

        var lineItems = Stream.concat(
            tkVersions.stream().map(tkV -> UtfGridTarifkanteConverter.toUtfGrid(tkV, mapRequest.getZoomLevel())),
            vkVersions.stream().map(vkV -> UtfGridVerkehrskanteConverter.toUtfGrid(vkV, mapRequest.getZoomLevel()))
        ).collect(Collectors.toList());

        var utfGrid = new UtfGrid(
            mapRequest.getWidth(),
            mapRequest.getHeight(),
            CoordinateConverter.convertToEpsg3857(bbox.getMinCoordinate()),
            CoordinateConverter.convertToEpsg3857(bbox.getMaxCoordinate()),
            pointItems,
            lineItems
        );
        logger.info("done");

        logger.info("rendering utf grid...");
        utfGrid.render();
        logger.info("done");

        logger.info("generating json...");
        var utfGridJson = this.utfGridService.createJson(utfGrid);
        logger.info("done");

        return new UtfGridResponse(utfGridJson);
    }
}
