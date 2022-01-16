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
import com.tschanz.geobooster.versioning_repo.model.VersionedObjectMap;
import com.tschanz.geobooster.zone_repo.service.ZonenplanRepo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Synchronized;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AwbRepoImpl implements AwbRepo {
    private static final Logger logger = LogManager.getLogger(AwbRepoImpl.class);
    private static final int DEBOUNCE_TIME_LAST_CHANGE_CHECK_SEC = 5;

    private final ConnectionState connectionState;
    private final AwbPersistence awbPersistence;
    private final ProgressState progressState;
    private final AwbRepoState awbRepoState;
    private final RgAuspraegungRepo rgAuspraegungRepo;
    private final ZonenplanRepo zonenplanRepo;
    private final TarifkanteRepo tkRepo;
    private final VerkehrskanteRepo vkRepo;

    private VersionedObjectMap<Awb, AwbVersion> versionedObjectMap;
    private LocalDateTime lastChangeCheck = LocalDateTime.now();


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
        Collection<Long> tkIds = rgaIds == null ? Collections.emptyList() : rgaIds.stream()
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
        Collection<Long> vkIds = zpIds == null ? Collections.emptyList() : zpIds.stream()
            .map(zpId -> this.zonenplanRepo.getElementVersionAtDate(zpId, date))
            .filter(Objects::nonNull)
            .flatMap(zpV -> zpV.getVerkehrskantenIds().stream())
            .distinct()
            .collect(Collectors.toList());

        var vkVs = vkIds.stream()
            .map(vkId -> this.vkRepo.getElementVersionAtDate(vkId, date))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        var filteredZpVkVs = vkVs.stream()
            .filter(vkV -> {
                // filter by bbox
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
        // skip check in subsequent requests within 5 seconds
        if (LocalDateTime.now().isBefore(this.lastChangeCheck.plusSeconds(DEBOUNCE_TIME_LAST_CHANGE_CHECK_SEC))) {
            return;
        }

        logger.info("checking for changes in awbs...");
        var changes = this.awbPersistence.findChanges(
            this.lastChangeCheck,
            this.versionedObjectMap.getAllVersionKeys()
        );

        if (!changes.getModifiedVersions().isEmpty()) {
            logger.info(String.format("new/changed awb versions found: %s", changes.getModifiedVersions()));
            changes.getModifiedElements().forEach(tkE -> this.versionedObjectMap.putElement(tkE));
            changes.getModifiedVersions().forEach(tkV -> this.versionedObjectMap.putVersion(tkV));
        }

        if (!changes.getDeletedVersionIds().isEmpty()) {
            logger.info(String.format("removed awb versions found: %s", changes.getDeletedVersionIds()));
            changes.getDeletedVersionIds().forEach(vId -> this.versionedObjectMap.deleteVersion(vId));
        }

        this.lastChangeCheck = LocalDateTime.now();
        logger.info("done.");
    }
}
