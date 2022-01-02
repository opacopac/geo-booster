package com.tschanz.geobooster.netz_cache.service;


import com.tschanz.geobooster.netz.model.Verwaltung;
import com.tschanz.geobooster.netz.model.VerwaltungVersion;
import com.tschanz.geobooster.netz.service.VerwaltungPersistenceRepo;
import com.tschanz.geobooster.netz.service.VerwaltungRepo;
import com.tschanz.geobooster.state.ProgressState;
import com.tschanz.geobooster.state_netz.VerwaltungState;
import com.tschanz.geobooster.versioning.model.VersionedObjectMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;


@Service
@RequiredArgsConstructor
public class VerwaltungCacheRepo implements VerwaltungRepo {
    private VersionedObjectMap<Verwaltung, VerwaltungVersion> versionedObjectMap;

    private final VerwaltungPersistenceRepo verwaltungPersistenceRepo;
    private final ProgressState progressState;
    private final VerwaltungState verwaltungState;


    @Override
    public void loadAll() {
        this.verwaltungState.updateIsLoading(true);

        this.progressState.updateProgressText("loading verwaltung...");
        var elements = this.verwaltungPersistenceRepo.readAllElements();
        this.verwaltungState.updateLoadedElementCount(elements.size());

        this.progressState.updateProgressText("loading verwaltung versions...");
        var versions = this.verwaltungPersistenceRepo.readAllVersions();
        this.verwaltungState.updateLoadedVersionCount(versions.size());

        this.progressState.updateProgressText("initializing verwaltung repo...");
        this.versionedObjectMap = new VersionedObjectMap<>(elements, versions);

        this.progressState.updateProgressText("loading verwaltung done");
        this.verwaltungState.updateIsLoading(false);
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
