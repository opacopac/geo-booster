package com.tschanz.geobooster.netz_cache.service;


import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz.model.Verkehrskante;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;
import com.tschanz.geobooster.netz.service.HaltestelleRepo;
import com.tschanz.geobooster.netz.service.VerkehrskanteRepo;
import com.tschanz.geobooster.netz.service.VerwaltungRepo;
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
public class VerkehrskanteCacheRepo implements VerkehrskanteRepo {
    private static final Logger logger = LogManager.getLogger(VerkehrskanteCacheRepo.class);

    private final HaltestelleRepo hstRepo;
    private final VerwaltungRepo verwaltungRepo;
    private final VerkehrskantePersistenceRepo vkPersistenceRepo;
    private Map<Long, Verkehrskante> elementMap;
    private Map<Long, VerkehrskanteVersion> versionMap;


    public Map<Long, Verkehrskante> getElementMap() {
        if (this.elementMap == null) {
            logger.info("loading all vk elements...");
            this.elementMap = this.vkPersistenceRepo.readAllElements(this.hstRepo.getElementMap());
            logger.info(String.format("cached %d vk elements.", this.elementMap.size()));
        }

        return this.elementMap;
    }


    public Map<Long, VerkehrskanteVersion> getVersionMap() {
        if (this.versionMap == null) {
            logger.info("loading all vk versions...");
            this.versionMap = this.vkPersistenceRepo.readAllVersions(this.getElementMap(), this.verwaltungRepo.getElementMap());
            logger.info(String.format("cached %d vk versions.", this.versionMap.size()));
        }

        return this.versionMap;
    }


    @Override
    public List<VerkehrskanteVersion> readVersions(LocalDate date, Extent extent, List<VerkehrsmittelTyp> vmTypes) {
        var minLon = CoordinateConverter.convertToEpsg4326(extent.getMinCoordinate()).getLongitude();
        var minLat = CoordinateConverter.convertToEpsg4326(extent.getMinCoordinate()).getLatitude();
        var maxLon = CoordinateConverter.convertToEpsg4326(extent.getMaxCoordinate()).getLongitude();
        var maxLat = CoordinateConverter.convertToEpsg4326(extent.getMaxCoordinate()).getLatitude();

        return this.getVersionMap().values()
            .stream()
            .filter(vkV -> date.isEqual(vkV.getVersionInfo().getGueltigVon()) || date.isAfter(vkV.getVersionInfo().getGueltigVon()))
            .filter(vkV -> date.isEqual(vkV.getVersionInfo().getGueltigBis()) || date.isBefore(vkV.getVersionInfo().getGueltigBis()))
            .filter(vkV -> vkV.hasOneOfVmTypes(vmTypes))
            //.filter(hstv -> hstv.getLng() >= minLon && hstv.getLng() <= maxLon)
            //.filter(hstv -> hstv.getLat() >= minLat && hstv.getLat() <= maxLat)
            .collect(Collectors.toList());
    }
}
