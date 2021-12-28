package com.tschanz.geobooster.netz_cache.service;

import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.netz.model.BetreiberVersion;
import com.tschanz.geobooster.netz.service.BetreiberRepo;
import com.tschanz.geobooster.versioning.model.VersionedObjectMap;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;


@Service
public class BetreiberCacheRepo implements BetreiberRepo {
    private VersionedObjectMap<Betreiber, BetreiberVersion> versionedObjectMap;


    @Override
    public void init(Collection<Betreiber> elements, Collection<BetreiberVersion> versions) {
        this.versionedObjectMap = new VersionedObjectMap<>(elements, versions);
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
