package com.tschanz.geobooster.zonen_repo.service;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz_repo.model.ProgressState;
import com.tschanz.geobooster.netz_repo.service.VerkehrskanteRepo;
import com.tschanz.geobooster.persistence_sql.model.ConnectionState;
import com.tschanz.geobooster.util.service.DebounceTimer;
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
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class ZonenplanRepoImpl implements ZonenplanRepo {
    private final ConnectionState connectionState;
    private final ZonenplanPersistence zonenplanPersistence;
    private final ZoneRepo zoneRepo;
    private final VerkehrskanteRepo vkRepo;
    private final ProgressState progressState;
    private final ZonenplanRepoState zonenplanRepoState;

    private Collection<Zonenplan> elements;
    private Collection<ZonenplanVersion> versions;
    private VersionedObjectMap<Zonenplan, ZonenplanVersion> versionedObjectMap;
    private final DebounceTimer debounceTimer = new DebounceTimer(5);


    @Override
    public void loadData() {
        this.zonenplanRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("loading zonenplan...");
        this.elements = this.zonenplanPersistence.readAllElements();
        this.zonenplanRepoState.updateLoadedElementCount(this.elements.size());

        this.progressState.updateProgressText("loading zonenplan versions...");
        this.versions = this.zonenplanPersistence.readAllVersions();
        this.zonenplanRepoState.updateLoadedVersionCount(this.versions.size());

        this.debounceTimer.touch();
        this.progressState.updateProgressText("loading zonenplan done");
        this.zonenplanRepoState.updateIsLoading(false);
    }


    @Override
    public void initRepo() {
        this.zonenplanRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("initializing zonenplan repo...");
        this.versionedObjectMap = new VersionedObjectMap<>(this.elements, this.versions);

        this.elements.clear();
        this.versions.clear();

        this.debounceTimer.touch();
        this.progressState.updateProgressText("initializing zonenplan done");
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
    public Collection<VerkehrskanteVersion> searchZpVerkehrskanten(long zonenplanId, LocalDate date, Extent<Epsg3857Coordinate> bbox) {
        var zoneVs = this.zoneRepo.getVersionsByZonenplanId(zonenplanId, date);
        return zoneVs.stream()
            .flatMap(zV -> {
                if (zV.getUrsprungsZoneId() != 0) {
                    var uzV = this.zoneRepo.getElementVersionAtDate(zV.getUrsprungsZoneId(), date);
                    if (uzV != null) {
                        return Stream.of(zV, uzV);
                    }
                }

                return Stream.of(zV);
            })
            .flatMap(zV -> this.zoneRepo.searchZoneVersionVks(zV.getId(), date, bbox).stream())
            .collect(Collectors.toList());
    }


    @SneakyThrows
    @Synchronized
    private void updateWhenChanged() {
        if (this.debounceTimer.isInDebounceTime()) {
            return;
        }

        var changes = this.zonenplanPersistence.findChanges(
            this.debounceTimer.getPreviousChangeCheck(),
            this.versionedObjectMap.getAllVersionKeys()
        );

        this.versionedObjectMap.updateChanges(changes);
    }
}
