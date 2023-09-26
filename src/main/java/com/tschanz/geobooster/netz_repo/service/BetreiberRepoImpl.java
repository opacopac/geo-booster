package com.tschanz.geobooster.netz_repo.service;

import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.netz.model.BetreiberVersion;
import com.tschanz.geobooster.netz_persistence.service.BetreiberPersistence;
import com.tschanz.geobooster.netz_repo.model.BetreiberRepoState;
import com.tschanz.geobooster.netz_repo.model.ProgressState;
import com.tschanz.geobooster.versioning_repo.model.VersionedObjectMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
@RequiredArgsConstructor
public class BetreiberRepoImpl implements BetreiberRepo {
    private final BetreiberPersistence betreiberPersistence;
    private final ProgressState progressState;
    private final BetreiberRepoState betreiberRepoState;

    private Collection<Betreiber> elements;
    private Collection<BetreiberVersion> versions;
    private VersionedObjectMap<Betreiber, BetreiberVersion> versionedObjectMap;


    @Override
    public void loadData() {
        this.betreiberRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("loading betreiber...");
        this.elements = this.betreiberPersistence.readAllElements();
        this.betreiberRepoState.updateLoadedElementCount(elements.size());

        this.progressState.updateProgressText("loading betreiber versions...");
        this.versions = this.betreiberPersistence.readAllVersions();
        this.betreiberRepoState.updateLoadedVersionCount(versions.size());

        this.progressState.updateProgressText("loading betreiber done");
        this.betreiberRepoState.updateIsLoading(false);
    }


    @Override
    public void initRepo() {
        this.betreiberRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("initializing betreiber repo...");
        this.versionedObjectMap = new VersionedObjectMap<>(this.elements, this.versions);

        this.elements.clear();
        this.versions.clear();

        this.progressState.updateProgressText("initializing betreiber done");
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
}
