package com.tschanz.geobooster.tarif_repo.service;

import com.tschanz.geobooster.netz_repo.model.ProgressState;
import com.tschanz.geobooster.tarif.model.Awb;
import com.tschanz.geobooster.tarif.model.AwbVersion;
import com.tschanz.geobooster.tarif_persistence.service.AwbPersistence;
import com.tschanz.geobooster.tarif_repo.model.AwbRepoState;
import com.tschanz.geobooster.versioning_repo.model.VersionedObjectMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;


@Service
@RequiredArgsConstructor
public class AwbRepoImpl implements AwbRepo {
    private final AwbPersistence awbPersistence;
    private final ProgressState progressState;
    private final AwbRepoState awbRepoState;

    private VersionedObjectMap<Awb, AwbVersion> versionedObjectMap;


    @Override
    public void loadAll() {
        this.awbRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("loading awbs...");
        var elements = this.awbPersistence.readAllElements();
        this.awbRepoState.updateLoadedElementCount(elements.size());

        this.progressState.updateProgressText("loading awb versions...");
        var versions = this.awbPersistence.readAllVersions();
        this.awbRepoState.updateLoadedVersionCount(versions.size());

        this.progressState.updateProgressText("initializing awb repo...");
        this.versionedObjectMap = new VersionedObjectMap<>(elements, versions);

        this.progressState.updateProgressText("loading awbs done");
        this.awbRepoState.updateIsLoading(false);
    }


    @Override
    public Awb getElement(long id) {
        return this.versionedObjectMap.getElement(id);
    }


    @Override
    public AwbVersion getVersion(long id) {
        return this.versionedObjectMap.getVersion(id);
    }


    @Override
    public Collection<AwbVersion> getElementVersions(long elementId) {
        return this.versionedObjectMap.getElementVersions(elementId);
    }


    @Override
    public AwbVersion getElementVersionAtDate(long elementId, LocalDate date) {
        return this.versionedObjectMap.getElementVersionAtDate(elementId, date);
    }
}
