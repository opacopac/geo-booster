package com.tschanz.geobooster.tarif_repo.service;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;
import com.tschanz.geobooster.netz_repo.model.ProgressState;
import com.tschanz.geobooster.netz_repo.service.VerkehrskanteRepo;
import com.tschanz.geobooster.persistence_sql.model.ConnectionState;
import com.tschanz.geobooster.rtm_repo.service.RgAuspraegungRepo;
import com.tschanz.geobooster.tarif.model.Awb;
import com.tschanz.geobooster.tarif.model.AwbIncVerwaltung;
import com.tschanz.geobooster.tarif.model.AwbVersion;
import com.tschanz.geobooster.tarif_persistence.service.AwbPersistence;
import com.tschanz.geobooster.tarif_repo.model.AwbRepoState;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.util.service.DebounceTimer;
import com.tschanz.geobooster.versioning.service.VersioningHelper;
import com.tschanz.geobooster.versioning_repo.model.VersionedObjectMap;
import com.tschanz.geobooster.zonen_repo.service.ZonenplanRepo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Synchronized;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
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
    private final VerkehrskanteRepo vkRepo;

    private Collection<Awb> elements;
    private Collection<AwbVersion> versions;
    private Collection<AwbIncVerwaltung> awbIncVerw;
    private VersionedObjectMap<Awb, AwbVersion> versionedObjectMap;
    private Map<Long, Collection<AwbIncVerwaltung>> awbIncVerwaltungenByAwbVIdMap;
    private final DebounceTimer debounceTimer = new DebounceTimer(5);


    @Override
    public void loadData() {
        this.awbRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("loading awbs...");
        this.elements = this.awbPersistence.readAllElements();
        this.awbRepoState.updateLoadedElementCount(elements.size());

        this.progressState.updateProgressText("loading awb versions...");
        this.versions = this.awbPersistence.readAllVersions();
        this.awbRepoState.updateLoadedVersionCount(versions.size());

        this.progressState.updateProgressText("loading awb inc verwaltung...");
        this.awbIncVerw = this.awbPersistence.readAllAwbIncVerwaltungen();

        this.debounceTimer.touch();
        this.progressState.updateProgressText("loading awbs done");
        this.awbRepoState.updateIsLoading(false);
    }


    @Override
    public void initRepo() {
        this.awbRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("initializing awb repo...");
        this.versionedObjectMap = new VersionedObjectMap<>(this.elements, this.versions);
        this.awbIncVerwaltungenByAwbVIdMap = ArrayHelper.create1toNLookupMap(this.awbIncVerw, AwbIncVerwaltung::getAwbVersionId, k -> k);

        this.elements.clear();
        this.versions.clear();
        this.awbIncVerw.clear();

        this.debounceTimer.touch();
        this.progressState.updateProgressText("initializing awbs done");
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
    public Collection<VerkehrskanteVersion> searchVerwaltungKanten(AwbVersion awbVersion, LocalDate date, Extent<Epsg3857Coordinate> bbox) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        var awbIncVerwaltungen = this.awbIncVerwaltungenByAwbVIdMap.get(awbVersion.getId());
        if (awbIncVerwaltungen == null) {
            return Collections.emptyList();
        }

        var awbVerwaltungIdMap = ArrayHelper.create1to1LookupMap(awbIncVerwaltungen, AwbIncVerwaltung::getVerwaltungId, AwbIncVerwaltung::getVerwaltungId);
        var vksByExtent = this.vkRepo.searchByExtent(bbox);

        return vksByExtent.stream()
            .filter(vkV -> VersioningHelper.isVersionInTimespan(vkV, date))
            .filter(vkV -> vkV.hasOneOfVerwaltungAndVmTypes(VerkehrsmittelTyp.ANY_EXCEPT_FUSSWEG, awbVerwaltungIdMap))
            .collect(Collectors.toList());
    }


    @Override
    public Collection<TarifkanteVersion> searchRgaTarifkanten(AwbVersion awbVersion, LocalDate date, Extent<Epsg3857Coordinate> bbox) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        var rgaIds = awbVersion.getIncludeRgaIds();
        return rgaIds == null ? Collections.<TarifkanteVersion>emptyList() : rgaIds.stream()
            .flatMap(rgaId -> this.rgAuspraegungRepo.searchRgaTarifkanten(rgaId, date, awbVersion.getPflegestatus(), bbox).stream())
            .distinct()
            .collect(Collectors.toList());
    }


    @Override
    public Collection<VerkehrskanteVersion> searchZpVerkehrskanten(AwbVersion awbVersion, LocalDate date, Extent<Epsg3857Coordinate> bbox) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        var zpIds = awbVersion.getIncludeZonenplanIds();
        return zpIds == null ? Collections.<VerkehrskanteVersion>emptyList() : zpIds.stream()
            .flatMap(zpId -> this.zonenplanRepo.searchZpVerkehrskanten(zpId, date, bbox).stream())
            .distinct()
            .collect(Collectors.toList());
    }


    @SneakyThrows
    @Synchronized
    private void updateWhenChanged() {
        if (this.debounceTimer.isInDebounceTime()) {
            return;
        }

        // awb version changes
        var versionChanges = this.awbPersistence.findAwbVersionChanges(
            this.debounceTimer.getPreviousChangeCheck(),
            this.versionedObjectMap.getAllVersionKeys()
        );
        this.versionedObjectMap.updateChanges(versionChanges);

        // awb include verwaltung changes
        var awbIncVerwaltungChanges = this.awbPersistence.findAwbIncVerwaltungChanges(
            this.debounceTimer.getPreviousChangeCheck(),
            this.awbIncVerwaltungenByAwbVIdMap.values().stream()
                .flatMap(Collection::stream)
                .map(AwbIncVerwaltung::getId)
                .collect(Collectors.toList())
        );

        // new/modified awbIncVerw
        awbIncVerwaltungChanges.getFirst().forEach(aiv -> {
            var aivs = this.awbIncVerwaltungenByAwbVIdMap.getOrDefault(aiv.getAwbVersionId(), Collections.emptyList());
            var newAivs = ArrayHelper.concatCollectionsDistinct(aivs, Collections.singletonList(aiv));
            this.awbIncVerwaltungenByAwbVIdMap.put(aiv.getAwbVersionId(), newAivs);
        });
        // deleted awbIncVerw
        if (!awbIncVerwaltungChanges.getSecond().isEmpty()) {
            var deletedAwbIncVerwIds = awbIncVerwaltungChanges.getSecond();
            for (var awbVId: this.awbIncVerwaltungenByAwbVIdMap.keySet()) {
                var awbIncVerwaltungen = this.awbIncVerwaltungenByAwbVIdMap.get(awbVId);
                var newAwbIncVerwaltungen = awbIncVerwaltungen.stream()
                    .filter(aiv -> !deletedAwbIncVerwIds.contains(aiv.getId()))
                    .collect(Collectors.toList());
                this.awbIncVerwaltungenByAwbVIdMap.put(awbVId, newAwbIncVerwaltungen);
            }
        }
    }
}
