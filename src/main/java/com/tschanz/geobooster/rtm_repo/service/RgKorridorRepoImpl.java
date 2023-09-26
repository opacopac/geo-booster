package com.tschanz.geobooster.rtm_repo.service;

import com.tschanz.geobooster.netz_repo.model.ProgressState;
import com.tschanz.geobooster.persistence_sql.model.ConnectionState;
import com.tschanz.geobooster.rtm.model.RgKorridor;
import com.tschanz.geobooster.rtm.model.RgKorridorVersion;
import com.tschanz.geobooster.rtm_persistence.service.RgKorridorPersistence;
import com.tschanz.geobooster.rtm_repo.model.RgKorridorRepoState;
import com.tschanz.geobooster.util.service.DebounceTimer;
import com.tschanz.geobooster.versioning.model.Pflegestatus;
import com.tschanz.geobooster.versioning_repo.model.VersionedObjectMap;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Synchronized;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RgKorridorRepoImpl implements RgKorridorRepo {
    private final ConnectionState connectionState;
    private final RgKorridorPersistence rgKorridorPersistence;
    private final ProgressState progressState;
    private final RgKorridorRepoState rgKorridorRepoState;

    private Collection<RgKorridor> elements;
    private Collection<RgKorridorVersion> versions;
    private VersionedObjectMap<RgKorridor, RgKorridorVersion> versionedObjectMap;
    private final DebounceTimer debounceTimer = new DebounceTimer(5);


    @Override
    public void loadData() {
        this.rgKorridorRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("loading rg korridore...");
        this.elements = this.rgKorridorPersistence.readAllElements();
        this.rgKorridorRepoState.updateLoadedElementCount(this.elements.size());

        this.progressState.updateProgressText("loading rg korridor versions...");
        this.versions = this.rgKorridorPersistence.readAllVersions();
        this.rgKorridorRepoState.updateLoadedVersionCount(this.versions.size());

        this.progressState.updateProgressText("loading rg korridore done");
        this.rgKorridorRepoState.updateIsLoading(false);
    }


    @Override
    public void initRepo() {
        this.rgKorridorRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("initializing rg korridor repo...");
        this.versionedObjectMap = new VersionedObjectMap<>(this.elements, this.versions);

        this.elements.clear();
        this.versions.clear();

        this.progressState.updateProgressText("initializing rg korridore done");
        this.rgKorridorRepoState.updateIsLoading(false);
    }


    @Override
    public RgKorridor getElement(long id) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionedObjectMap.getElement(id);
    }


    @Override
    public RgKorridorVersion getVersion(long id) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionedObjectMap.getVersion(id);
    }


    @Override
    public Collection<RgKorridorVersion> getElementVersions(long elementId) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionedObjectMap.getElementVersions(elementId);
    }


    @Override
    public RgKorridorVersion getElementVersionAtDate(long elementId, LocalDate date, Pflegestatus minStatus) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionedObjectMap.getElementVersionAtDate(elementId, date, minStatus);
    }


    @Override
    public Collection<RgKorridor> getElementsByRgId(long relationsgebietId) {
        return this.versionedObjectMap.getAllElements().stream()
            .filter(zE -> zE.getRelationsgbietId() == relationsgebietId)
            .collect(Collectors.toList());
    }


    @Override
    public Collection<RgKorridorVersion> getVersionsByRgId(long relationsgebietId, LocalDate date, Pflegestatus minStatus) {
        var rgKorridorEs = this.getElementsByRgId(relationsgebietId);

        return rgKorridorEs.stream()
            .map(zE -> this.getElementVersionAtDate(zE.getId(), date, minStatus))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }


    @SneakyThrows
    @Synchronized
    private void updateWhenChanged() {
        if (this.debounceTimer.isInDebounceTime()) {
            return;
        }

        var changes = this.rgKorridorPersistence.findChanges(
            this.debounceTimer.getPreviousChangeCheck(),
            this.versionedObjectMap.getAllVersionKeys()
        );

        this.versionedObjectMap.updateChanges(changes);
    }
}
