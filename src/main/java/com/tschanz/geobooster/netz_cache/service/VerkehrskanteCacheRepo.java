package com.tschanz.geobooster.netz_cache.service;


import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz.model.Verkehrskante;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz.service.HaltestellenRepo;
import com.tschanz.geobooster.netz.service.VerkehrskantenRepo;
import com.tschanz.geobooster.netz_persistence.service.VerkehrskantePersistenceRepo;
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
public class VerkehrskanteCacheRepo implements VerkehrskantenRepo {
    private static final Logger logger = LogManager.getLogger(VerkehrskanteCacheRepo.class);

    private final VerkehrskantePersistenceRepo vkPersistenceRepo;
    private final HaltestellenRepo hstRepo;
    private Map<Long, Verkehrskante> vkElementMap;
    private Map<Long, VerkehrskanteVersion> vkVersionMap;


    public Map<Long, Verkehrskante> getVkElementMap() {
        if (this.vkElementMap == null) {
            logger.info("loading all vk elements...");
            this.vkElementMap = this.vkPersistenceRepo.readAllElements(this.hstRepo.getHstElementMap());
            logger.info(String.format("cached %d vk elements.", this.vkElementMap.size()));
        }

        return this.vkElementMap;
    }


    public Map<Long, VerkehrskanteVersion> getVkVersionMap() {
        if (this.vkVersionMap == null) {
            logger.info("loading all vk versions...");
            this.vkVersionMap = this.vkPersistenceRepo.readAllVersions(this.getVkElementMap());
            logger.info(String.format("cached %d vk versions.", this.vkVersionMap.size()));
        }

        return this.vkVersionMap;
    }


    @Override
    public List<VerkehrskanteVersion> readVerkehrskanteVersions(LocalDate date, Extent extent) {
        var minLon = CoordinateConverter.convertToEpsg4326(extent.getMinCoordinate()).getLongitude();
        var minLat = CoordinateConverter.convertToEpsg4326(extent.getMinCoordinate()).getLatitude();
        var maxLon = CoordinateConverter.convertToEpsg4326(extent.getMaxCoordinate()).getLongitude();
        var maxLat = CoordinateConverter.convertToEpsg4326(extent.getMaxCoordinate()).getLatitude();

        return this.getVkVersionMap().values()
            .stream()
            .filter(hstv -> date.isEqual(hstv.getVersionInfo().getGueltigVon()) || date.isAfter(hstv.getVersionInfo().getGueltigVon()))
            .filter(hstv -> date.isEqual(hstv.getVersionInfo().getGueltigBis()) || date.isBefore(hstv.getVersionInfo().getGueltigBis()))
            //.filter(hstv -> hstv.getLng() >= minLon && hstv.getLng() <= maxLon)
            //.filter(hstv -> hstv.getLat() >= minLat && hstv.getLat() <= maxLat)
            .collect(Collectors.toList());
    }
}
