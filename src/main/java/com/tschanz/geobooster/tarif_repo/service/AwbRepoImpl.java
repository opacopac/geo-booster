package com.tschanz.geobooster.tarif_repo.service;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz_repo.model.ProgressState;
import com.tschanz.geobooster.netz_repo.service.TarifkanteRepo;
import com.tschanz.geobooster.netz_repo.service.VerkehrskanteRepo;
import com.tschanz.geobooster.persistence_sql.model.ConnectionState;
import com.tschanz.geobooster.rtm_repo.service.RgAuspraegungRepo;
import com.tschanz.geobooster.tarif.model.Awb;
import com.tschanz.geobooster.tarif.model.AwbVersion;
import com.tschanz.geobooster.tarif_persistence.service.AwbPersistence;
import com.tschanz.geobooster.tarif_repo.model.AwbRepoState;
import com.tschanz.geobooster.util.service.DebounceTimer;
import com.tschanz.geobooster.versioning_repo.model.VersionedObjectMap;
import com.tschanz.geobooster.zonen_repo.service.ZonenplanRepo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Synchronized;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AwbRepoImpl implements AwbRepo {
    private final ConnectionState connectionState;
    private final AwbPersistence awbPersistence;
    private final ProgressState progressState;
    private final AwbRepoState awbRepoState;
    private final RgAuspraegungRepo rgAuspraegungRepo;
    private final ZonenplanRepo zonenplanRepo;
    private final TarifkanteRepo tkRepo;
    private final VerkehrskanteRepo vkRepo;

    private VersionedObjectMap<Awb, AwbVersion> versionedObjectMap;
    private final DebounceTimer debounceTimer = new DebounceTimer(5);


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

        this.debounceTimer.touch();
        this.progressState.updateProgressText("loading awbs done");
        this.awbRepoState.updateIsLoading(false);
    }


    @Override
    public Awb getElement(long id) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionedObjectMap.getElement(id);
    }


    @Override
    public AwbVersion getVersion(long id) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionedObjectMap.getVersion(id);
    }


    @Override
    public Collection<AwbVersion> getElementVersions(long elementId) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionedObjectMap.getElementVersions(elementId);
    }


    @Override
    public AwbVersion getElementVersionAtDate(long elementId, LocalDate date) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionedObjectMap.getElementVersionAtDate(elementId, date);
    }


    @Override
    public Collection<VerkehrskanteVersion> searchVerwaltungKanten(AwbVersion awbVersion, LocalDate date, Extent<Epsg3857Coordinate> bbox) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        Map<Long, Long> awbVerwaltungIdMap = new HashMap<>();
        awbVersion.getIncludeVerwaltungIds().forEach(verwEid -> awbVerwaltungIdMap.put(verwEid, verwEid));
        var vksByExtent = this.vkRepo.searchByExtent(bbox);

        return vksByExtent.stream()
            .filter(vkV -> vkV.hasOneOfVerwaltungIds(awbVerwaltungIdMap))
            .collect(Collectors.toList());
    }


    @Override
    public Collection<TarifkanteVersion> searchRgaTarifkanten(AwbVersion awbVersion, LocalDate date, Extent<Epsg3857Coordinate> bbox) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        var rgaIds = awbVersion.getIncludeRgaIds();
        var tkIds = rgaIds == null ? Collections.<Long>emptyList() : rgaIds.stream()
            .map(rgaId -> this.rgAuspraegungRepo.getElementVersionAtDate(rgaId, date))
            .filter(Objects::nonNull)
            .flatMap(rgaV -> rgaV.getTarifkantenIds().stream())
            .distinct()
            .collect(Collectors.toList());

        var tkVs = tkIds.stream()
            .map(tkId -> this.tkRepo.getElementVersionAtDate(tkId, date))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        var filteredRgaTkVs = tkVs.stream()
            .filter(tkV -> {
                // filter by bbox
                var tkExtent = Extent.fromCoords(
                    this.tkRepo.getStartCoordinate(tkV),
                    this.tkRepo.getEndCoordinate(tkV)
                );

                return tkExtent.isExtentIntersecting(bbox);
            })
            .collect(Collectors.toList());

        return filteredRgaTkVs;
    }


    @Override
    public Collection<VerkehrskanteVersion> searchZpVerkehrskanten(AwbVersion awbVersion, LocalDate date, Extent<Epsg3857Coordinate> bbox) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        var zpIds = awbVersion.getIncludeZonenplanIds();
        var vkVs = zpIds == null ? Collections.<VerkehrskanteVersion>emptyList() : zpIds.stream()
            .map(zpId -> this.zonenplanRepo.getElementVersionAtDate(zpId, date))
            .filter(Objects::nonNull)
            .flatMap(zpV -> this.zonenplanRepo.searchZpVerkehrskanten(zpV, date, bbox).stream())
            .distinct()
            .collect(Collectors.toList());

        return vkVs;
    }


    @SneakyThrows
    @Synchronized
    private void updateWhenChanged() {
        if (this.debounceTimer.isInDebounceTime()) {
            return;
        }

        var changes = this.awbPersistence.findChanges(
            this.debounceTimer.getPreviousChangeCheck(),
            this.versionedObjectMap.getAllVersionKeys()
        );

        this.versionedObjectMap.updateChanges(changes);
    }
}
