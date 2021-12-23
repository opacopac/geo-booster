package com.tschanz.geobooster.netz_cache.service;


import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
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
public class HaltestelleCacheRepoImpl implements HaltestelleCacheRepo {
    private static final Logger logger = LogManager.getLogger(HaltestelleCacheRepoImpl.class);

    private final HaltestellenPersistenceRepo haltestellenPersistenceRepo;
    private Map<Long, Haltestelle> elementMap;
    private Map<Long, HaltestelleVersion> versionMap;


    public Map<Long, Haltestelle> getElementMap() {
        if (this.elementMap == null) {
            logger.info("loading all hst elements...");
            this.elementMap = this.haltestellenPersistenceRepo.readAllElements();
            logger.info(String.format("cached %d hst elements.", this.elementMap.size()));
        }

        return this.elementMap;
    }


    public Map<Long, HaltestelleVersion> getVersionMap() {
        if (this.versionMap == null) {
            logger.info("loading all hst versions...");
            this.versionMap = this.haltestellenPersistenceRepo.readAllVersions(this.getElementMap());
            logger.info(String.format("cached %d hst versions.", this.versionMap.size()));
        }

        return this.versionMap;
    }


    @Override
    public List<HaltestelleVersion> readVersions(LocalDate date, Extent extent) {
        var minLon = CoordinateConverter.convertToEpsg4326(extent.getMinCoordinate()).getLongitude();
        var minLat = CoordinateConverter.convertToEpsg4326(extent.getMinCoordinate()).getLatitude();
        var maxLon = CoordinateConverter.convertToEpsg4326(extent.getMaxCoordinate()).getLongitude();
        var maxLat = CoordinateConverter.convertToEpsg4326(extent.getMaxCoordinate()).getLatitude();

        return this.getVersionMap().values()
            .stream()
            .filter(hstv -> date.isEqual(hstv.getVersionInfo().getGueltigVon()) || date.isAfter(hstv.getVersionInfo().getGueltigVon()))
            .filter(hstv -> date.isEqual(hstv.getVersionInfo().getGueltigBis()) || date.isBefore(hstv.getVersionInfo().getGueltigBis()))
            .filter(hstv -> hstv.getCoordinate().getLongitude() >= minLon && hstv.getCoordinate().getLongitude() <= maxLon)
            .filter(hstv -> hstv.getCoordinate().getLatitude() >= minLat && hstv.getCoordinate().getLatitude() <= maxLat)
            .collect(Collectors.toList());
    }
}
