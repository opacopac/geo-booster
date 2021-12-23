package com.tschanz.geobooster.netz_cache.service;

import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.netz.model.BetreiberVersion;
import com.tschanz.geobooster.netz_persistence.service.BetreiberPersistenceRepo;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class BetreiberCacheRepoImpl implements BetreiberCacheRepo {
    private static final Logger logger = LogManager.getLogger(BetreiberCacheRepoImpl.class);

    private final BetreiberPersistenceRepo betreiberPersistenceRepo;
    private Map<Long, Betreiber> elementMap;
    private Map<Long, BetreiberVersion> versionMap;


    public Map<Long, Betreiber> getElementMap() {
        if (this.elementMap == null) {
            logger.info("loading all betreiber elements...");
            this.elementMap = this.betreiberPersistenceRepo.readAllElements();
            logger.info(String.format("cached %d betreiber elements.", this.elementMap.size()));
        }

        return this.elementMap;
    }


    public Map<Long, BetreiberVersion> getVersionMap() {
        if (this.versionMap == null) {
            logger.info("loading all betreiber versions...");
            this.versionMap = this.betreiberPersistenceRepo.readAllVersions(this.getElementMap());
            logger.info(String.format("cached %d betreiber versions.", this.versionMap.size()));
        }

        return this.versionMap;
    }
}
