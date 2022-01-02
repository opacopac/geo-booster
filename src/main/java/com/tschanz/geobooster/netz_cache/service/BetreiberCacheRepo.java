package com.tschanz.geobooster.netz_cache.service;

import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.netz.model.BetreiberVersion;
import com.tschanz.geobooster.netz.service.BetreiberPersistenceRepo;
import com.tschanz.geobooster.netz.service.BetreiberRepo;
import com.tschanz.geobooster.state.ProgressState;
import com.tschanz.geobooster.state_netz.BetreiberState;
import com.tschanz.geobooster.versioning.model.VersionedObjectMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;


@Service
@RequiredArgsConstructor
public class BetreiberCacheRepo implements BetreiberRepo {
    private final BetreiberPersistenceRepo betreiberPersistenceRepo;
    private final ProgressState progressState;
    private final BetreiberState betreiberState;

    private VersionedObjectMap<Betreiber, BetreiberVersion> versionedObjectMap;


    @Override
    public void loadAll() {
        this.betreiberState.updateIsLoading(true);

        this.progressState.updateProgressText("loading betreiber...");
        var elements = this.betreiberPersistenceRepo.readAllElements();
        this.betreiberState.updateLoadedElementCount(elements.size());

        this.progressState.updateProgressText("loading betreiber versions...");
        var versions = this.betreiberPersistenceRepo.readAllVersions();
        this.betreiberState.updateLoadedVersionCount(versions.size());

        this.progressState.updateProgressText("initializing betreiber repo...");
        this.versionedObjectMap = new VersionedObjectMap<>(elements, versions);

        this.progressState.updateProgressText("loading betreiber done");
        this.betreiberState.updateIsLoading(false);
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
