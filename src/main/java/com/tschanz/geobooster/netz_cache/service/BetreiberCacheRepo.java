package com.tschanz.geobooster.netz_cache.service;

import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.netz.model.BetreiberVersion;
import com.tschanz.geobooster.netz.service.BetreiberPersistenceRepo;
import com.tschanz.geobooster.netz.service.BetreiberRepo;
import com.tschanz.geobooster.versioning.model.VersionedObjectMap;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;


@Service
@RequiredArgsConstructor
public class BetreiberCacheRepo implements BetreiberRepo {
    private static final Logger logger = LogManager.getLogger(BetreiberCacheRepo.class);

    private final BetreiberPersistenceRepo betreiberPersistenceRepo;
    private VersionedObjectMap<Betreiber, BetreiberVersion> versionedObjectMap;


    @Override
    public void init() {
        logger.info("loading betreiber data...");
        this.versionedObjectMap = new VersionedObjectMap<>(
            this.betreiberPersistenceRepo.readAllElements(),
            this.betreiberPersistenceRepo.readAllVersions()
        );
        logger.info(String.format("%d elements / %d versions cached", this.versionedObjectMap.getAllElements().size(),
            this.versionedObjectMap.getAllVersions().size()));
    }


    @Override
    public Betreiber getElement(long id) {
        return this.versionedObjectMap.getElement(id);
    }


    @Override
    public BetreiberVersion getVersion(long id) {
        return this.versionedObjectMap.getVersion(id);
    }


    @Override
    public Collection<BetreiberVersion> getElementVersions(long elementId) {
        return this.versionedObjectMap.getElementVersions(elementId);
    }


    @Override
    public BetreiberVersion getElementVersionAtDate(long elementId, LocalDate date) {
        return this.versionedObjectMap.getElementVersionAtDate(elementId, date);
    }
}
