package com.tschanz.geobooster.netz_cache.service;


import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz.model.Tarifkante;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;
import com.tschanz.geobooster.netz.service.HaltestelleRepo;
import com.tschanz.geobooster.netz.service.TarifkanteRepo;
import com.tschanz.geobooster.netz_persistence.service.TarifkantePersistenceRepo;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TarifkanteCacheRepo implements TarifkanteRepo {
    private static final Logger logger = LogManager.getLogger(TarifkanteCacheRepo.class);

    private final TarifkantePersistenceRepo tkPersistenceRepo;
    private final HaltestelleRepo hstRepo;
    private Map<Long, Tarifkante> elementMap;
    private Map<Long, TarifkanteVersion> versionMap;


    public Map<Long, Tarifkante> getElementMap() {
        if (this.elementMap == null) {
            logger.info("loading all tk elements...");
            this.elementMap = this.tkPersistenceRepo.readAllElements(this.hstRepo.getElementMap());
            logger.info(String.format("cached %d tk elements.", this.elementMap.size()));
        }

        return this.elementMap;
    }


    public Map<Long, TarifkanteVersion> getVersionMap() {
        if (this.versionMap == null) {
            logger.info("loading all tk versions...");
            this.versionMap = this.tkPersistenceRepo.readAllVersions(this.getElementMap());
            logger.info(String.format("cached %d tk versions.", this.versionMap.size()));
        }

        return this.versionMap;
    }


    @Override
    public List<TarifkanteVersion> readVersions(LocalDate date, Extent extent, List<VerkehrsmittelTyp> vmTypes) {
        var minLon = CoordinateConverter.convertToEpsg4326(extent.getMinCoordinate()).getLongitude();
        var minLat = CoordinateConverter.convertToEpsg4326(extent.getMinCoordinate()).getLatitude();
        var maxLon = CoordinateConverter.convertToEpsg4326(extent.getMaxCoordinate()).getLongitude();
        var maxLat = CoordinateConverter.convertToEpsg4326(extent.getMaxCoordinate()).getLatitude();

        return this.getVersionMap().values().stream()
            //.filter(tkV -> tkV.hasOneOfVmTypes(vmTypes))
            .filter(tkV -> date.isEqual(tkV.getVersionInfo().getGueltigVon()) || date.isAfter(tkV.getVersionInfo().getGueltigVon()))
            .filter(tkV -> date.isEqual(tkV.getVersionInfo().getGueltigBis()) || date.isBefore(tkV.getVersionInfo().getGueltigBis()))
            //.filter(hstv -> hstv.getLng() >= minLon && hstv.getLng() <= maxLon)
            //.filter(hstv -> hstv.getLat() >= minLat && hstv.getLat() <= maxLat)
            .collect(Collectors.toList());
    }
}
