package com.tschanz.geobooster.netz_repo.service;

import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.netz.model.BetreiberVersion;
import com.tschanz.geobooster.netz_persistence.service.BetreiberPersistence;
import com.tschanz.geobooster.netz_repo.model.BetreiberRepoState;
import com.tschanz.geobooster.netz_repo.model.ProgressState;
import com.tschanz.geobooster.versioning_repo.model.VersionedObjectMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;


@Service
@RequiredArgsConstructor
public class BetreiberRepoImpl implements BetreiberRepo {
    private final BetreiberPersistence betreiberPersistence;
    private final ProgressState progressState;
    private final BetreiberRepoState betreiberRepoState;

    private VersionedObjectMap<Betreiber, BetreiberVersion> versionedObjectMap;


    @Override
    public void loadAll() {
        this.betreiberRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("loading betreiber...");
        var elements = this.betreiberPersistence.readAllElements();
        this.betreiberRepoState.updateLoadedElementCount(elements.size());

        this.progressState.updateProgressText("loading betreiber versions...");
        var versions = this.betreiberPersistence.readAllVersions();
        this.betreiberRepoState.updateLoadedVersionCount(versions.size());

        this.progressState.updateProgressText("initializing betreiber repo...");
        this.versionedObjectMap = new VersionedObjectMap<>(elements, versions);

        this.progressState.updateProgressText("loading betreiber done");
        this.betreiberRepoState.updateIsLoading(false);
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
