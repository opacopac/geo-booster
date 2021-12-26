package com.tschanz.geobooster.netz_cache.service;


import com.tschanz.geobooster.netz.model.Verwaltung;
import com.tschanz.geobooster.netz.model.VerwaltungVersion;
import com.tschanz.geobooster.netz_persistence.service.VerwaltungPersistenceRepo;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class VerwaltungCacheRepoImpl implements VerwaltungCacheRepo {
    private static final Logger logger = LogManager.getLogger(VerwaltungCacheRepoImpl.class);

    private final BetreiberCacheRepo betreiberRepo;
    private final VerwaltungPersistenceRepo verwaltungPersistenceRepo;
    private Map<Long, Verwaltung> elementMap;
    private Map<Long, VerwaltungVersion> versionMap;


    @Override
    public void init() {
        this.getElementMap();
        this.getVersionMap();
    }


    public Map<Long, Verwaltung> getElementMap() {
        if (this.elementMap == null) {
            logger.info("loading all verwaltung elements...");
            this.elementMap = this.verwaltungPersistenceRepo.readAllElements(this.betreiberRepo.getElementMap());
            logger.info(String.format("cached %d verwaltung elements.", this.elementMap.size()));
        }

        return this.elementMap;
    }


    public Map<Long, VerwaltungVersion> getVersionMap() {
        if (this.versionMap == null) {
            logger.info("loading all verwaltung versions...");
            this.versionMap = this.verwaltungPersistenceRepo.readAllVersions(this.getElementMap());
            logger.info(String.format("cached %d verwaltung versions.", this.versionMap.size()));
        }

        return this.versionMap;
    }
}
