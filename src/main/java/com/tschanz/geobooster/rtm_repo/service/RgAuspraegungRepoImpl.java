package com.tschanz.geobooster.rtm_repo.service;

import com.tschanz.geobooster.netz_repo.model.ProgressState;
import com.tschanz.geobooster.rtm.model.RgAuspraegung;
import com.tschanz.geobooster.rtm.model.RgAuspraegungVersion;
import com.tschanz.geobooster.rtm_persistence.service.RgAuspraegungPersistence;
import com.tschanz.geobooster.rtm_repo.model.RgAuspraegungRepoState;
import com.tschanz.geobooster.versioning_repo.model.VersionedObjectMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;


@Service
@RequiredArgsConstructor
public class RgAuspraegungRepoImpl implements RgAuspraegungRepo {
    private final RgAuspraegungPersistence rgAuspraegungPersistence;
    private final ProgressState progressState;
    private final RgAuspraegungRepoState rgAuspraegungRepoState;

    private VersionedObjectMap<RgAuspraegung, RgAuspraegungVersion> versionedObjectMap;


    @Override
    public void loadAll() {
        this.rgAuspraegungRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("loading rg auspraegungen...");
        var elements = this.rgAuspraegungPersistence.readAllElements();
        this.rgAuspraegungRepoState.updateLoadedElementCount(elements.size());

        this.progressState.updateProgressText("loading rg auspraegung versions...");
        var versions = this.rgAuspraegungPersistence.readAllVersions(elements);
        this.rgAuspraegungRepoState.updateLoadedVersionCount(versions.size());

        this.progressState.updateProgressText("initializing rg auspraegung repo...");
        this.versionedObjectMap = new VersionedObjectMap<>(elements, versions);

        this.progressState.updateProgressText("loading rg auspraegungen done");
        this.rgAuspraegungRepoState.updateIsLoading(false);
    }


    @Override
    public RgAuspraegung getElement(long id) {
        return this.versionedObjectMap.getElement(id);
    }


    @Override
    public RgAuspraegungVersion getVersion(long id) {
        return this.versionedObjectMap.getVersion(id);
    }


    @Override
    public Collection<RgAuspraegungVersion> getElementVersions(long elementId) {
        return this.versionedObjectMap.getElementVersions(elementId);
    }


    @Override
    public RgAuspraegungVersion getElementVersionAtDate(long elementId, LocalDate date) {
        return this.versionedObjectMap.getElementVersionAtDate(elementId, date);
    }
}
