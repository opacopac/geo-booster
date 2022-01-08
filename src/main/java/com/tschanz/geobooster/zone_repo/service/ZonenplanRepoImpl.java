package com.tschanz.geobooster.zone_repo.service;

import com.tschanz.geobooster.netz_repo.model.ProgressState;
import com.tschanz.geobooster.versioning_repo.model.VersionedObjectMap;
import com.tschanz.geobooster.zone_repo.model.ZonenplanRepoState;
import com.tschanz.geobooster.zonen.model.Zonenplan;
import com.tschanz.geobooster.zonen.model.ZonenplanVersion;
import com.tschanz.geobooster.zonen_persistence.service.ZonenplanPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;


@Service
@RequiredArgsConstructor
public class ZonenplanRepoImpl implements ZonenplanRepo {
    private final ZonenplanPersistence zonenplanPersistence;
    private final ProgressState progressState;
    private final ZonenplanRepoState zonenplanRepoState;

    private VersionedObjectMap<Zonenplan, ZonenplanVersion> versionedObjectMap;


    @Override
    public void loadAll() {
        this.zonenplanRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("loading zonenplan...");
        var elements = this.zonenplanPersistence.readAllElements();
        this.zonenplanRepoState.updateLoadedElementCount(elements.size());

        this.progressState.updateProgressText("loading zonenplan versions...");
        var versions = this.zonenplanPersistence.readAllVersions(elements);
        this.zonenplanRepoState.updateLoadedVersionCount(versions.size());

        this.progressState.updateProgressText("initializing zonenplan repo...");
        this.versionedObjectMap = new VersionedObjectMap<>(elements, versions);

        this.progressState.updateProgressText("loading zonenplan done");
        this.zonenplanRepoState.updateIsLoading(false);
    }


    @Override
    public Zonenplan getElement(long id) {
        return this.versionedObjectMap.getElement(id);
    }


    @Override
    public ZonenplanVersion getVersion(long id) {
        return this.versionedObjectMap.getVersion(id);
    }


    @Override
    public Collection<ZonenplanVersion> getElementVersions(long elementId) {
        return this.versionedObjectMap.getElementVersions(elementId);
    }


    @Override
    public ZonenplanVersion getElementVersionAtDate(long elementId, LocalDate date) {
        return this.versionedObjectMap.getElementVersionAtDate(elementId, date);
    }
}
