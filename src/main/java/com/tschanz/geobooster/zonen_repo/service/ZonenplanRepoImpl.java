package com.tschanz.geobooster.zonen_repo.service;

import com.tschanz.geobooster.netz_repo.model.ProgressState;
import com.tschanz.geobooster.persistence_sql.model.ConnectionState;
import com.tschanz.geobooster.versioning_repo.model.VersionedObjectMap;
import com.tschanz.geobooster.zonen.model.Zonenplan;
import com.tschanz.geobooster.zonen.model.ZonenplanVersion;
import com.tschanz.geobooster.zonen_persistence.service.ZonenplanPersistence;
import com.tschanz.geobooster.zonen_repo.model.ZonenplanRepoState;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Synchronized;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;


@Service
@RequiredArgsConstructor
public class ZonenplanRepoImpl implements ZonenplanRepo {
    private static final int DEBOUNCE_TIME_LAST_CHANGE_CHECK_SEC = 5;

    private final ConnectionState connectionState;
    private final ZonenplanPersistence zonenplanPersistence;
    private final ProgressState progressState;
    private final ZonenplanRepoState zonenplanRepoState;

    private VersionedObjectMap<Zonenplan, ZonenplanVersion> versionedObjectMap;
    private LocalDateTime lastChangeCheck = LocalDateTime.now();


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
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionedObjectMap.getElement(id);
    }


    @Override
    public ZonenplanVersion getVersion(long id) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionedObjectMap.getVersion(id);
    }


    @Override
    public Collection<ZonenplanVersion> getElementVersions(long elementId) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionedObjectMap.getElementVersions(elementId);
    }


    @Override
    public ZonenplanVersion getElementVersionAtDate(long elementId, LocalDate date) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionedObjectMap.getElementVersionAtDate(elementId, date);
    }


    @SneakyThrows
    @Synchronized
    private void updateWhenChanged() {
        // skip check in subsequent requests within 5 seconds
        if (LocalDateTime.now().isBefore(this.lastChangeCheck.plusSeconds(DEBOUNCE_TIME_LAST_CHANGE_CHECK_SEC))) {
            return;
        }

        var changes = this.zonenplanPersistence.findChanges(
            this.lastChangeCheck,
            this.versionedObjectMap.getAllVersionKeys(),
            this.versionedObjectMap.getAllElements()
        );

        this.versionedObjectMap.updateChanges(changes);

        this.lastChangeCheck = LocalDateTime.now();
    }
}
