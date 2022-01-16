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
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ZonenplanRepoImpl implements ZonenplanRepo {
    private final ConnectionState connectionState;
    private final ZonenplanPersistence zonenplanPersistence;
    private final ZoneRepo zoneRepo;
    private final VerkehrskanteRepo vkRepo;
    private final ProgressState progressState;
    private final ZonenplanRepoState zonenplanRepoState;

    private VersionedObjectMap<Zonenplan, ZonenplanVersion> versionedObjectMap;
    private final DebounceTimer debounceTimer = new DebounceTimer(5);


    @Override
    public void loadAll() {
        this.zonenplanRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("loading zonenplan...");
        var elements = this.zonenplanPersistence.readAllElements();
        this.zonenplanRepoState.updateLoadedElementCount(elements.size());

        this.progressState.updateProgressText("loading zonenplan versions...");
        var versions = this.zonenplanPersistence.readAllVersions();
        this.zonenplanRepoState.updateLoadedVersionCount(versions.size());

        this.progressState.updateProgressText("initializing zonenplan repo...");
        this.versionedObjectMap = new VersionedObjectMap<>(elements, versions);

        this.debounceTimer.touch();
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


    @Override
    public Collection<VerkehrskanteVersion> searchZpVerkehrskanten(long zonenplanId, LocalDate date, Extent<Epsg3857Coordinate> bbox) {
        var zoneVs = this.zoneRepo.getVersionsByZonenplanId(zonenplanId, date);
        var vkVs = zoneVs.stream()
            .map(zV -> zV.getUrsprungsZoneId() == 0 ? zV : this.zoneRepo.getElementVersionAtDate(zV.getUrsprungsZoneId(), date))
            .flatMap(zV -> zV.getVerkehrskantenIds().stream())
            .map(vkEId -> this.vkRepo.getElementVersionAtDate(vkEId, date))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        var filteredZpVkVs = vkVs.stream()
            .filter(vkV -> {
                var vkExtent = Extent.fromCoords(
                    this.vkRepo.getStartCoordinate(vkV),
                    this.vkRepo.getEndCoordinate(vkV)
                );

                return vkExtent.isExtentIntersecting(bbox);
            })
            .collect(Collectors.toList());

        return filteredZpVkVs;
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
