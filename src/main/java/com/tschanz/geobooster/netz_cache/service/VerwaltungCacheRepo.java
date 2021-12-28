package com.tschanz.geobooster.netz_cache.service;


import com.tschanz.geobooster.netz.model.Verwaltung;
import com.tschanz.geobooster.netz.model.VerwaltungVersion;
import com.tschanz.geobooster.netz.service.VerwaltungPersistenceRepo;
import com.tschanz.geobooster.netz.service.VerwaltungRepo;
import com.tschanz.geobooster.versioning.model.VersionedObjectMap;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;


@Service
@RequiredArgsConstructor
public class VerwaltungCacheRepo implements VerwaltungRepo {
    private static final Logger logger = LogManager.getLogger(VerwaltungCacheRepo.class);

    private final VerwaltungPersistenceRepo verwaltungPersistenceRepo;
    private VersionedObjectMap<Verwaltung, VerwaltungVersion> versionedObjectMap;


    @Override
    public void init() {
        logger.info("loading verwaltung data...");
        this.versionedObjectMap = new VersionedObjectMap<>(
            this.verwaltungPersistenceRepo.readAllElements(),
            this.verwaltungPersistenceRepo.readAllVersions()
        );
        logger.info(String.format("%d elements / %d versions cached", this.versionedObjectMap.getAllElements().size(),
            this.versionedObjectMap.getAllVersions().size()));
    }


    @Override
    public Verwaltung getElement(long id) {
        return this.versionedObjectMap.getElement(id);
    }


    @Override
    public VerwaltungVersion getVersion(long id) {
        return this.versionedObjectMap.getVersion(id);
    }


    @Override
    public Collection<VerwaltungVersion> getElementVersions(long elementId) {
        return this.versionedObjectMap.getElementVersions(elementId);
    }


    @Override
    public VerwaltungVersion getElementVersionAtDate(long elementId, LocalDate date) {
        return this.versionedObjectMap.getElementVersionAtDate(elementId, date);
    }
}
