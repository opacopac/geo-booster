package com.tschanz.geobooster.zonen_repo.service;

import com.tschanz.geobooster.netz_repo.model.ProgressState;
import com.tschanz.geobooster.persistence_sql.model.ConnectionState;
import com.tschanz.geobooster.util.service.DebounceTimer;
import com.tschanz.geobooster.versioning_repo.model.VersionedObjectMap;
import com.tschanz.geobooster.zonen.model.Zone;
import com.tschanz.geobooster.zonen.model.ZoneVersion;
import com.tschanz.geobooster.zonen_persistence.service.ZonePersistence;
import com.tschanz.geobooster.zonen_repo.model.ZoneRepoState;
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
public class ZoneRepoImpl implements ZoneRepo {
    private final ConnectionState connectionState;
    private final ZonePersistence zonePersistence;
    private final ProgressState progressState;
    private final ZoneRepoState zoneRepoState;

    private VersionedObjectMap<Zone, ZoneVersion> versionedObjectMap;
    private final DebounceTimer debounceTimer = new DebounceTimer(5);


    @Override
    public void loadAll() {
        this.zoneRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("loading zonen...");
        var elements = this.zonePersistence.readAllElements();
        this.zoneRepoState.updateLoadedElementCount(elements.size());

        this.progressState.updateProgressText("loading zone versions...");
        var versions = this.zonePersistence.readAllVersions();
        this.zoneRepoState.updateLoadedVersionCount(versions.size());

        this.progressState.updateProgressText("initializing zone repo...");
        this.versionedObjectMap = new VersionedObjectMap<>(elements, versions);

        this.debounceTimer.touch();
        this.progressState.updateProgressText("loading zonen done");
        this.zoneRepoState.updateIsLoading(false);
    }


    @Override
    public Zone getElement(long id) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionedObjectMap.getElement(id);
    }


    @Override
    public ZoneVersion getVersion(long id) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionedObjectMap.getVersion(id);
    }


    @Override
    public Collection<ZoneVersion> getElementVersions(long elementId) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionedObjectMap.getElementVersions(elementId);
    }


    @Override
    public ZoneVersion getElementVersionAtDate(long elementId, LocalDate date) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionedObjectMap.getElementVersionAtDate(elementId, date);
    }



    @Override
    public Collection<Zone> getElementsByZonenplanId(long zonenplanId) {
        return this.versionedObjectMap.getAllElements().stream()
            .filter(zE -> zE.getZonenplanId() == zonenplanId)
            .collect(Collectors.toList());
    }


    @Override
    public Collection<ZoneVersion> getVersionsByZonenplanId(long zonenplanId, LocalDate date) {
        var zoneEs = this.getElementsByZonenplanId(zonenplanId);

        return zoneEs.stream()
            .map(zE -> this.getElementVersionAtDate(zE.getId(), date))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }


    @SneakyThrows
    @Synchronized
    private void updateWhenChanged() {
        if (this.debounceTimer.isInDebounceTime()) {
            return;
        }

        var changes = this.zonePersistence.findChanges(
            this.debounceTimer.getPreviousChangeCheck(),
            this.versionedObjectMap.getAllVersionKeys()
        );

        this.versionedObjectMap.updateChanges(changes);
    }
}
