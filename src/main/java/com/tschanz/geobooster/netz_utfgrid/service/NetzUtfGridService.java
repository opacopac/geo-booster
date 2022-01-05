package com.tschanz.geobooster.netz_utfgrid.service;

import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz_repo.service.HaltestelleRepo;
import com.tschanz.geobooster.netz_repo.service.LinieVarianteRepo;
import com.tschanz.geobooster.netz_repo.service.TarifkanteRepo;
import com.tschanz.geobooster.netz_repo.service.VerkehrskanteRepo;
import com.tschanz.geobooster.netz_utfgrid.model.*;
import com.tschanz.geobooster.utfgrid.model.UtfGrid;
import com.tschanz.geobooster.utfgrid.service.UtfGridService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class NetzUtfGridService {
    private static final Logger logger = LogManager.getLogger(NetzUtfGridService.class);

    private final HaltestelleRepo haltestelleRepo;
    private final VerkehrskanteRepo verkehrskanteRepo;
    private final TarifkanteRepo tarifkanteRepo;
    private final LinieVarianteRepo linieVarianteRepo;
    private final UtfGridService utfGridService;


    public NetzUtfGridResponse getResponse(NetzUtfGridRequest request) {
        List<HaltestelleVersion> hstVersions = request.isShowHaltestellen()
            ? this.haltestelleRepo.searchVersions(request.getDate(), request.getBbox())
            : Collections.emptyList();

        Collection<VerkehrskanteVersion> vkVersions;
        Collection<TarifkanteVersion> tkVersions;
        if (request.getLinieVarianteIds().size() > 0 && (request.isShowVerkehrskanten() || request.isShowTarifkanten() || request.isShowUnmappedTarifkanten())) {
            vkVersions = request.isShowVerkehrskanten()
                ? this.linieVarianteRepo.searchVerkehrskanteVersions(request.getLinieVarianteIds(), request.getVmTypes(), request.getDate())
                : Collections.emptyList();
            tkVersions = request.isShowTarifkanten() || request.isShowUnmappedTarifkanten()
                ? this.linieVarianteRepo.searchTarifkanteVersions(request.getLinieVarianteIds(), request.getVmTypes(), request.getDate())
                : Collections.emptyList();
        } else {
            vkVersions = request.isShowVerkehrskanten()
                ? this.verkehrskanteRepo.searchVersionsByExtent(request.getDate(), request.getBbox(), request.getVmTypes(), request.getVerwaltungVersionIds(), request.isShowTerminiert())
                : Collections.emptyList();
            tkVersions = request.isShowTarifkanten() || request.isShowUnmappedTarifkanten()
                ? this.tarifkanteRepo.searchVersionsByExtent(request.getDate(), request.getBbox(), request.getVmTypes(), request.getVerwaltungVersionIds(), request.isShowUnmappedTarifkanten())
                : Collections.emptyList();
        }

        logger.info("initializing utf grid...");
        var utfGridHstConverter = new UtfGridHaltestelleConverter(this.haltestelleRepo);
        var pointItems = hstVersions.stream()
            .map(hst -> utfGridHstConverter.toUtfGrid(hst, request.getZoomLevel()))
            .collect(Collectors.toList());

        var utfGridTkConverter = new UtfGridTarifkanteConverter(this.tarifkanteRepo);
        var utfGridVkConverter = new UtfGridVerkehrskanteConverter(this.verkehrskanteRepo);
        var lineItems = Stream.concat(
            tkVersions.stream().map(tkV -> utfGridTkConverter.toUtfGrid(tkV, request.getZoomLevel(), request.isShowUnmappedTarifkanten())),
            vkVersions.stream().map(vkV -> utfGridVkConverter.toUtfGrid(vkV, request.getZoomLevel()))
        ).collect(Collectors.toList());

        var utfGrid = new UtfGrid(
            request.getWidth(),
            request.getHeight(),
            CoordinateConverter.convertToEpsg3857(request.getBbox().getMinCoordinate()),
            CoordinateConverter.convertToEpsg3857(request.getBbox().getMaxCoordinate()),
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

        return new NetzUtfGridResponse(utfGridJson);
    }
}
