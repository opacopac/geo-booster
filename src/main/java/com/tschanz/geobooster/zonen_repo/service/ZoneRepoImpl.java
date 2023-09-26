package com.tschanz.geobooster.zonen_repo.service;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz_repo.model.ProgressState;
import com.tschanz.geobooster.netz_repo.service.VerkehrskanteRepo;
import com.tschanz.geobooster.persistence_sql.model.ConnectionState;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.util.service.DebounceTimer;
import com.tschanz.geobooster.versioning.model.Pflegestatus;
import com.tschanz.geobooster.versioning_repo.model.VersionedObjectMap;
import com.tschanz.geobooster.zonen.model.Zone;
import com.tschanz.geobooster.zonen.model.ZoneVersion;
import com.tschanz.geobooster.zonen.model.ZoneVkZuordnung;
import com.tschanz.geobooster.zonen_persistence.service.ZonePersistence;
import com.tschanz.geobooster.zonen_repo.model.ZoneRepoState;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Synchronized;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ZoneRepoImpl implements ZoneRepo {
    private final ConnectionState connectionState;
    private final ZonePersistence zonePersistence;
    private final VerkehrskanteRepo vkRepo;
    private final ProgressState progressState;
    private final ZoneRepoState zoneRepoState;

    private Collection<Zone> elements;
    private Collection<ZoneVersion> versions;
    private Collection<ZoneVkZuordnung> zoneVkZuordnungen;
    private VersionedObjectMap<Zone, ZoneVersion> versionedObjectMap;
    private Map<Long, Collection<ZoneVkZuordnung>> zoneVkZuordnungenByZoneVIdMap;
    private final DebounceTimer debounceTimer = new DebounceTimer(5);


    @Override
    public void loadData() {
        this.zoneRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("loading zonen...");
        this.elements = this.zonePersistence.readAllElements();
        this.zoneRepoState.updateLoadedElementCount(this.elements.size());

        this.progressState.updateProgressText("loading zone versions...");
        this.versions = this.zonePersistence.readAllVersions();
        this.zoneRepoState.updateLoadedVersionCount(this.versions.size());

        this.progressState.updateProgressText("loading zone vk zuordnungen...");
        this.zoneVkZuordnungen = this.zonePersistence.readAllZoneVkZuordnung();

        this.debounceTimer.touch();
        this.progressState.updateProgressText("loading zonen done");
        this.zoneRepoState.updateIsLoading(false);
    }


    @Override
    public void initRepo() {
        this.zoneRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("initializing zone repo...");
        this.versionedObjectMap = new VersionedObjectMap<>(this.elements, this.versions);
        this.zoneVkZuordnungenByZoneVIdMap = ArrayHelper.create1toNLookupMap(this.zoneVkZuordnungen, ZoneVkZuordnung::getZoneVersionId, k -> k);

        this.elements.clear();
        this.versions.clear();
        this.zoneVkZuordnungen.clear();

        this.debounceTimer.touch();
        this.progressState.updateProgressText("initializing zonen done");
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

        return this.versionedObjectMap.getElementVersionAtDate(elementId, date, Pflegestatus.PRODUKTIV);
    }



    @Override
    public Collection<Zone> getElementsByZonenplanId(long zonenplanId) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionedObjectMap.getAllElements().stream()
            .filter(zE -> zE.getZonenplanId() == zonenplanId)
            .collect(Collectors.toList());
    }


    @Override
    public Collection<ZoneVersion> getVersionsByZonenplanId(long zonenplanId, LocalDate date) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        var zoneEs = this.getElementsByZonenplanId(zonenplanId);

        return zoneEs.stream()
            .map(zE -> this.getElementVersionAtDate(zE.getId(), date))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }


    @Override
    public Collection<VerkehrskanteVersion> searchZoneVersionVks(long zoneVersionId, LocalDate date, Extent<Epsg3857Coordinate> bbox) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        var zoneVkZuordnungen = this.zoneVkZuordnungenByZoneVIdMap.getOrDefault(zoneVersionId, Collections.emptyList());
        var vkVs = zoneVkZuordnungen.stream()
            .map(ZoneVkZuordnung::getVerkehrskanteId)
            .map(vkId -> this.vkRepo.getElementVersionAtDate(vkId, date))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        return vkVs.stream()
            .filter(vkV -> {
                var vkExtent = Extent.fromCoords(
                    this.vkRepo.getStartCoordinate(vkV),
                    this.vkRepo.getEndCoordinate(vkV)
                );

                return vkExtent.isExtentIntersecting(bbox);
            })
            .collect(Collectors.toList());
    }


    @SneakyThrows
    @Synchronized
    private void updateWhenChanged() {
        if (this.debounceTimer.isInDebounceTime()) {
            return;
        }

        // zone changes
        var changes = this.zonePersistence.findZoneVersionChanges(
            this.debounceTimer.getPreviousChangeCheck(),
            this.versionedObjectMap.getAllVersionKeys()
        );

        this.versionedObjectMap.updateChanges(changes);

        // zone vk zuordnung changes
        var zoneVkZuordnungChanges = this.zonePersistence.findZoneVkZuordnungChanges(
            this.debounceTimer.getPreviousChangeCheck(),
            this.zoneVkZuordnungenByZoneVIdMap.values().stream()
                .flatMap(Collection::stream)
                .map(ZoneVkZuordnung::getId)
                .collect(Collectors.toList())
        );

        // new/modified zone vk zuordnung
        zoneVkZuordnungChanges.getFirst().forEach(aiv -> {
            var zvzs = this.zoneVkZuordnungenByZoneVIdMap.getOrDefault(aiv.getZoneVersionId(), Collections.emptyList());
            var newZvzs = ArrayHelper.concatCollectionsDistinct(zvzs, Collections.singletonList(aiv));
            this.zoneVkZuordnungenByZoneVIdMap.put(aiv.getZoneVersionId(), newZvzs);
        });
        // deleted zone vk zuordnung
        if (!zoneVkZuordnungChanges.getSecond().isEmpty()) {
            var deletedZvzIds = zoneVkZuordnungChanges.getSecond();
            for (var zoneVid: this.zoneVkZuordnungenByZoneVIdMap.keySet()) {
                var zvzs = this.zoneVkZuordnungenByZoneVIdMap.get(zoneVid);
                var newZvzs = zvzs.stream()
                    .filter(zvz -> !deletedZvzIds.contains(zvz.getId()))
                    .collect(Collectors.toList());
                this.zoneVkZuordnungenByZoneVIdMap.put(zoneVid, newZvzs);
            }
        }
    }
}
