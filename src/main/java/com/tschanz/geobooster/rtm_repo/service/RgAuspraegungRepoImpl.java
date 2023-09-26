package com.tschanz.geobooster.rtm_repo.service;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz_repo.model.ProgressState;
import com.tschanz.geobooster.netz_repo.service.TarifkanteRepo;
import com.tschanz.geobooster.persistence_sql.model.ConnectionState;
import com.tschanz.geobooster.rtm.model.RgAuspraegung;
import com.tschanz.geobooster.rtm.model.RgAuspraegungVersion;
import com.tschanz.geobooster.rtm_persistence.service.RgAuspraegungPersistence;
import com.tschanz.geobooster.rtm_repo.model.RgAuspraegungRepoState;
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
public class RgAuspraegungRepoImpl implements RgAuspraegungRepo {
    private final ConnectionState connectionState;
    private final RgKorridorRepo rgKorridorRepo;
    private final TarifkanteRepo tkRepo;
    private final RgAuspraegungPersistence rgAuspraegungPersistence;
    private final ProgressState progressState;
    private final RgAuspraegungRepoState rgAuspraegungRepoState;

    private Collection<RgAuspraegung> elements;
    private Collection<RgAuspraegungVersion> versions;
    private VersionedObjectMap<RgAuspraegung, RgAuspraegungVersion> versionedObjectMap;
    private final DebounceTimer debounceTimer = new DebounceTimer(5);


    @Override
    public void loadData() {
        this.rgAuspraegungRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("loading rg auspraegungen...");
        this.elements = this.rgAuspraegungPersistence.readAllElements();
        this.rgAuspraegungRepoState.updateLoadedElementCount(this.elements.size());

        this.progressState.updateProgressText("loading rg auspraegung versions...");
        this.versions = this.rgAuspraegungPersistence.readAllVersions();
        this.rgAuspraegungRepoState.updateLoadedVersionCount(this.versions.size());

        this.progressState.updateProgressText("loading rg auspraegungen done");
        this.rgAuspraegungRepoState.updateIsLoading(false);
    }


    @Override
    public void initRepo() {
        this.rgAuspraegungRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("initializing rg auspraegung repo...");
        this.versionedObjectMap = new VersionedObjectMap<>(this.elements, this.versions);

        this.elements.clear();
        this.versions.clear();

        this.progressState.updateProgressText("initializing rg auspraegungen done");
        this.rgAuspraegungRepoState.updateIsLoading(false);
    }


    @Override
    public RgAuspraegung getElement(long id) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionedObjectMap.getElement(id);
    }


    @Override
    public RgAuspraegungVersion getVersion(long id) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionedObjectMap.getVersion(id);
    }


    @Override
    public Collection<RgAuspraegungVersion> getElementVersions(long elementId) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionedObjectMap.getElementVersions(elementId);
    }


    @Override
    public Collection<TarifkanteVersion> searchRgaTarifkanten(long rgaId, LocalDate date, Pflegestatus minStatus, Extent<Epsg3857Coordinate> bbox) {
        var rgaE = this.getElement(rgaId);
        var tkVs = this.rgKorridorRepo.getVersionsByRgId(rgaE.getRelationsgebietId(), date, minStatus).stream()
            .flatMap(korrV -> korrV.getTarifkantenIds().stream())
            .map(tkEId -> this.tkRepo.getElementVersionAtDate(tkEId, date))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        var filteredRgaTkVs = tkVs.stream()
            .filter(tkV -> {
                var tkExtent = Extent.fromCoords(
                    this.tkRepo.getStartCoordinate(tkV),
                    this.tkRepo.getEndCoordinate(tkV)
                );

                return tkExtent.isExtentIntersecting(bbox);
            })
            .collect(Collectors.toList());

        return filteredRgaTkVs;
    }


    @SneakyThrows
    @Synchronized
    private void updateWhenChanged() {
        if (this.debounceTimer.isInDebounceTime()) {
            return;
        }

        var changes = this.rgAuspraegungPersistence.findChanges(
            this.debounceTimer.getPreviousChangeCheck(),
            this.versionedObjectMap.getAllVersionKeys()
        );

        this.versionedObjectMap.updateChanges(changes);
    }
}
