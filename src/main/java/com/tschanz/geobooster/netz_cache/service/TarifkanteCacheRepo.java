package com.tschanz.geobooster.netz_cache.service;


import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz.model.Tarifkante;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
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
    private Map<Long, Tarifkante> tkElementMap;
    private Map<Long, TarifkanteVersion> tkVersionMap;


    public Map<Long, Tarifkante> getTkElementMap() {
        if (this.tkElementMap == null) {
            logger.info("loading all tk elements...");
            this.tkElementMap = this.tkPersistenceRepo.readAllElements(this.hstRepo.getHstElementMap());
            logger.info(String.format("cached %d tk elements.", this.tkElementMap.size()));
        }

        return this.tkElementMap;
    }


    public Map<Long, TarifkanteVersion> getTkVersionMap() {
        if (this.tkVersionMap == null) {
            logger.info("loading all tk versions...");
            this.tkVersionMap = this.tkPersistenceRepo.readAllVersions(this.getTkElementMap());
            logger.info(String.format("cached %d tk versions.", this.tkVersionMap.size()));
        }

        return this.tkVersionMap;
    }


    @Override
    public List<TarifkanteVersion> readTarifkanteVersions(LocalDate date, Extent extent) {
        var minLon = CoordinateConverter.convertToEpsg4326(extent.getMinCoordinate()).getLongitude();
        var minLat = CoordinateConverter.convertToEpsg4326(extent.getMinCoordinate()).getLatitude();
        var maxLon = CoordinateConverter.convertToEpsg4326(extent.getMaxCoordinate()).getLongitude();
        var maxLat = CoordinateConverter.convertToEpsg4326(extent.getMaxCoordinate()).getLatitude();

        return this.getTkVersionMap().values()
            .stream()
            .filter(hstv -> date.isEqual(hstv.getVersionInfo().getGueltigVon()) || date.isAfter(hstv.getVersionInfo().getGueltigVon()))
            .filter(hstv -> date.isEqual(hstv.getVersionInfo().getGueltigBis()) || date.isBefore(hstv.getVersionInfo().getGueltigBis()))
            //.filter(hstv -> hstv.getLng() >= minLon && hstv.getLng() <= maxLon)
            //.filter(hstv -> hstv.getLat() >= minLat && hstv.getLat() <= maxLat)
            .collect(Collectors.toList());
    }
}
