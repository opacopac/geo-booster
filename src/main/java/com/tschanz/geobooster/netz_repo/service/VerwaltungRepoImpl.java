package com.tschanz.geobooster.netz_repo.service;


import com.tschanz.geobooster.netz.model.Verwaltung;
import com.tschanz.geobooster.netz.model.VerwaltungVersion;
import com.tschanz.geobooster.netz_persistence.service.VerwaltungPersistence;
import com.tschanz.geobooster.netz_repo.model.ProgressState;
import com.tschanz.geobooster.netz_repo.model.VerwaltungRepoState;
import com.tschanz.geobooster.versioning_repo.model.VersionedObjectMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;


@Service
@RequiredArgsConstructor
public class VerwaltungRepoImpl implements VerwaltungRepo {
    private VersionedObjectMap<Verwaltung, VerwaltungVersion> versionedObjectMap;

    private final VerwaltungPersistence verwaltungPersistence;
    private final ProgressState progressState;
    private final VerwaltungRepoState verwaltungRepoState;


    @Override
    public void loadAll() {
        this.verwaltungRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("loading verwaltung...");
        var elements = this.verwaltungPersistence.readAllElements();
        this.verwaltungRepoState.updateLoadedElementCount(elements.size());

        this.progressState.updateProgressText("loading verwaltung versions...");
        var versions = this.verwaltungPersistence.readAllVersions();
        this.verwaltungRepoState.updateLoadedVersionCount(versions.size());

        this.progressState.updateProgressText("initializing verwaltung repo...");
        this.versionedObjectMap = new VersionedObjectMap<>(elements, versions);

        this.progressState.updateProgressText("loading verwaltung done");
        this.verwaltungRepoState.updateIsLoading(false);
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
