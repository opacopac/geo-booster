package com.tschanz.geobooster.netz_cache.service;


import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz.service.HaltestelleRepo;
import com.tschanz.geobooster.netz_persistence.service.HaltestellenPersistenceRepo;
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
public class HaltestelleCacheRepo implements HaltestelleRepo {
    private static final Logger logger = LogManager.getLogger(HaltestelleCacheRepo.class);

    private final HaltestellenPersistenceRepo haltestellenPersistenceRepo;
    private Map<Long, Haltestelle> hstElementMap;
    private Map<Long, HaltestelleVersion> hstVersionMap;


    public Map<Long, Haltestelle> getHstElementMap() {
        if (this.hstElementMap == null) {
            logger.info("loading all hst elements...");
            this.hstElementMap = this.haltestellenPersistenceRepo.readAllElements();
            logger.info(String.format("cached %d hst elements.", this.hstElementMap.size()));
        }

        return this.hstElementMap;
    }


    public Map<Long, HaltestelleVersion> getHstVersionMap() {
        if (this.hstVersionMap == null) {
            logger.info("loading all hst versions...");
            this.hstVersionMap = this.haltestellenPersistenceRepo.readAllVersions(this.getHstElementMap());
            logger.info(String.format("cached %d hst versions.", this.hstVersionMap.size()));
        }

        return this.hstVersionMap;
    }


    @Override
    public List<HaltestelleVersion> readHaltestellenVersions(LocalDate date, Extent extent) {
        var minLon = CoordinateConverter.convertToEpsg4326(extent.getMinCoordinate()).getLongitude();
        var minLat = CoordinateConverter.convertToEpsg4326(extent.getMinCoordinate()).getLatitude();
        var maxLon = CoordinateConverter.convertToEpsg4326(extent.getMaxCoordinate()).getLongitude();
        var maxLat = CoordinateConverter.convertToEpsg4326(extent.getMaxCoordinate()).getLatitude();

        return this.getHstVersionMap().values()
            .stream()
            .filter(hstv -> date.isEqual(hstv.getVersionInfo().getGueltigVon()) || date.isAfter(hstv.getVersionInfo().getGueltigVon()))
            .filter(hstv -> date.isEqual(hstv.getVersionInfo().getGueltigBis()) || date.isBefore(hstv.getVersionInfo().getGueltigBis()))
            .filter(hstv -> hstv.getCoordinate().getLongitude() >= minLon && hstv.getCoordinate().getLongitude() <= maxLon)
            .filter(hstv -> hstv.getCoordinate().getLatitude() >= minLat && hstv.getCoordinate().getLatitude() <= maxLat)
            .collect(Collectors.toList());
    }
}
