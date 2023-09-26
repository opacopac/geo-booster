package com.tschanz.geobooster.rtm_repo.service;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz_repo.model.ProgressState;
import com.tschanz.geobooster.netz_repo.model.QuadTreeConfig;
import com.tschanz.geobooster.netz_repo.service.HaltestelleRepo;
import com.tschanz.geobooster.persistence_sql.model.ConnectionState;
import com.tschanz.geobooster.quadtree.model.QuadTree;
import com.tschanz.geobooster.quadtree.model.QuadTreeItem;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabe;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabeVersion;
import com.tschanz.geobooster.rtm_persistence.service.HaltestelleWegangabePersistence;
import com.tschanz.geobooster.rtm_repo.model.HaltestelleWegangabeRepoState;
import com.tschanz.geobooster.util.service.DebounceTimer;
import com.tschanz.geobooster.versioning_repo.model.VersionedObjectMap;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Synchronized;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class HaltestelleWegangabeRepoImpl implements HaltestelleWegangabeRepo {
    private final ConnectionState connectionState;
    private final HaltestelleRepo hstRepo;
    private final HaltestelleWegangabePersistence hstWegangabePersistence;
    private final ProgressState progressState;
    private final HaltestelleWegangabeRepoState hstWegangabeRepoState;
    private final DebounceTimer debounceTimer = new DebounceTimer(5);

    private Collection<HaltestelleWegangabe> elements;
    private Collection<HaltestelleWegangabeVersion> versions;
    private VersionedObjectMap<HaltestelleWegangabe, HaltestelleWegangabeVersion> versionedObjectMap;
    private QuadTree<HaltestelleWegangabeVersion> versionQuadTree;


    @Override
    public void loadData() {
        this.hstWegangabeRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("loading haltestelle wegangaben...");
        this.elements = this.hstWegangabePersistence.readAllElements();
        this.hstWegangabeRepoState.updateLoadedElementCount(this.elements.size());

        this.progressState.updateProgressText("loading haltestelle wegangabe versions...");
        this.versions = this.hstWegangabePersistence.readAllVersions();
        this.hstWegangabeRepoState.updateLoadedVersionCount(this.versions.size());

        this.progressState.updateProgressText("loading haltestelle wegangaben done");
        this.hstWegangabeRepoState.updateIsLoading(false);
    }


    @Override
    public void initRepo() {
        this.hstWegangabeRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("initializing haltestelle wegangaben repo...");
        this.versionedObjectMap = new VersionedObjectMap<>(this.elements, this.versions);

        this.versionQuadTree = new QuadTree<>(QuadTreeConfig.MAX_TREE_DEPTH);
        this.versionQuadTree.build(this.versions, this::getHaltestelleCoordinate);

        this.elements.clear();
        this.versions.clear();

        this.progressState.updateProgressText("initializing haltestelle wegangaben done");
        this.hstWegangabeRepoState.updateIsLoading(false);
    }


    @Override
    public List<HaltestelleWegangabeVersion> searchByExtent(Extent<Epsg3857Coordinate> extent) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionQuadTree.findItems(extent).stream()
            .map(QuadTreeItem::getItem)
            .collect(Collectors.toList());
    }


    @Override
    public HaltestelleVersion getHaltestelleVersion(HaltestelleWegangabeVersion hstWegangabeVersion) {
        var hstWaE = this.getElement(hstWegangabeVersion.getElementId());
        var hstE = this.hstRepo.getByUic(hstWaE.getUicCode());

        return hstE != null
            ? this.hstRepo.getElementVersionAtDate(hstE.getId(), hstWegangabeVersion.getGueltigBis())
            : null;
    }


    @Override
    public Epsg3857Coordinate getHaltestelleCoordinate(HaltestelleWegangabeVersion hstWegangabeVersion) {
        var hstV = this.getHaltestelleVersion(hstWegangabeVersion);

        return hstV != null
            ? hstV.getCoordinate()
            : null;
    }


    @Override
    public HaltestelleWegangabe getElement(long id) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionedObjectMap.getElement(id);
    }


    @Override
    public HaltestelleWegangabeVersion getVersion(long id) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionedObjectMap.getVersion(id);
    }


    @Override
    public Collection<HaltestelleWegangabeVersion> getElementVersions(long elementId) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionedObjectMap.getElementVersions(elementId);
    }


    @SneakyThrows
    @Synchronized
    private void updateWhenChanged() {
        if (this.debounceTimer.isInDebounceTime()) {
            return;
        }

        var changes = this.hstWegangabePersistence.findChanges(
            this.debounceTimer.getPreviousChangeCheck(),
            this.versionedObjectMap.getAllVersionKeys()
        );

        this.versionedObjectMap.updateChanges(changes);

        if (!changes.getModifiedVersions().isEmpty()) {
            changes.getModifiedVersions().forEach(hstWaV -> {
                this.versionQuadTree.removeItem(hstWaV.getId());

                var coord = this.getHaltestelleCoordinate(hstWaV);
                if (coord != null) {
                    var item = new QuadTreeItem<>(coord, hstWaV);
                    this.versionQuadTree.addItem(item);
                }
            });
        }

        if (!changes.getDeletedVersionIds().isEmpty()) {
            changes.getDeletedVersionIds().forEach(vId -> {
                this.versionQuadTree.removeItem(vId);
            });
        }
    }
}
