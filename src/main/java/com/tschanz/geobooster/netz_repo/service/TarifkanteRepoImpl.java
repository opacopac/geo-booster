package com.tschanz.geobooster.netz_repo.service;


import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.*;
import com.tschanz.geobooster.netz_persistence.service.TarifkantePersistence;
import com.tschanz.geobooster.netz_repo.model.ProgressState;
import com.tschanz.geobooster.netz_repo.model.QuadTreeConfig;
import com.tschanz.geobooster.netz_repo.model.TarifkanteRepoState;
import com.tschanz.geobooster.persistence_sql.model.ConnectionState;
import com.tschanz.geobooster.quadtree.model.AreaQuadTree;
import com.tschanz.geobooster.quadtree.model.AreaQuadTreeItem;
import com.tschanz.geobooster.quadtree.model.QuadTreeCoordinate;
import com.tschanz.geobooster.quadtree.model.QuadTreeExtent;
import com.tschanz.geobooster.util.service.DebounceTimer;
import com.tschanz.geobooster.versioning_repo.model.VersionedObjectMap;
import io.reactivex.subjects.PublishSubject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Synchronized;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TarifkanteRepoImpl implements TarifkanteRepo {
    private static final Logger logger = LogManager.getLogger(TarifkanteRepoImpl.class);

    private final TarifkantePersistence tkPersistenceRepo;
    private final HaltestelleRepo hstRepo;
    private final VerkehrskanteRepo verkehrskanteRepo;
    private final ProgressState progressState;
    private final TarifkanteRepoState tarifkanteRepoState;
    private final ConnectionState connectionState;

    private Collection<Tarifkante> elements;
    private Collection<TarifkanteVersion> versions;
    private VersionedObjectMap<Tarifkante, TarifkanteVersion> versionedObjectMap;
    private AreaQuadTree<TarifkanteVersion> versionQuadTree;
    private final DebounceTimer debounceTimer = new DebounceTimer(5);

    @Getter
    private final PublishSubject<Boolean> tkUpdateSubject = PublishSubject.create();




    @Override
    public void loadData() {
        this.tarifkanteRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("loading tarifkanten...");
        this.elements = this.tkPersistenceRepo.readAllElements();
        this.tarifkanteRepoState.updateLoadedElementCount(this.elements.size());

        this.progressState.updateProgressText("loading tarifkante versions...");
        this.versions = this.tkPersistenceRepo.readAllVersions();
        this.tarifkanteRepoState.updateLoadedVersionCount(this.versions.size());

        this.debounceTimer.touch();
        this.progressState.updateProgressText("loading tarifkanten done");
        this.tarifkanteRepoState.updateIsLoading(false);
    }


    @Override
    public void initRepo() {
        this.tarifkanteRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("initializing tarifkante repo...");
        this.versionedObjectMap = new VersionedObjectMap<>(this.elements, this.versions);
        this.versionQuadTree = this.createQuadTree(this.versionedObjectMap);

        this.elements.clear();
        this.versions.clear();

        this.debounceTimer.touch();
        this.progressState.updateProgressText("initializing tarifkanten done");
        this.tarifkanteRepoState.updateIsLoading(false);
    }


    @Override
    public Tarifkante getElement(long id) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionedObjectMap.getElement(id);
    }


    @Override
    public TarifkanteVersion getVersion(long id) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionedObjectMap.getVersion(id);
    }


    @Override
    public Collection<TarifkanteVersion> getElementVersions(long elementId) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionedObjectMap.getElementVersions(elementId);
    }


    @Override
    public TarifkanteVersion getElementVersionAtDate(long elementId, LocalDate date) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionedObjectMap.getElementVersionAtDate(elementId, date);
    }


    @Override
    public List<TarifkanteVersion> searchByExtent(Extent<Epsg3857Coordinate> extent) {
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

        return this.versionQuadTree
            .findItems(this.getQuadTreeExtent(extent.getMinCoordinate(), extent.getMaxCoordinate()))
            .stream()
            .map(AreaQuadTreeItem::getItem)
            .collect(Collectors.toList());
    }


    @Override
    public Haltestelle getStartHaltestelle(TarifkanteVersion tkVersion) {
        var tkE = this.getElement(tkVersion.getElementId());

        return this.hstRepo.getElement(tkE.getHaltestelle1Id());
    }


    @Override
    public Haltestelle getEndHaltestelle(TarifkanteVersion tkVersion) {
        var tkE = this.getElement(tkVersion.getElementId());

        return this.hstRepo.getElement(tkE.getHaltestelle2Id());
    }


    @Override
    public HaltestelleVersion getStartHaltestelleVersion(TarifkanteVersion tkVersion) {
        var tkE = this.getElement(tkVersion.getElementId());

        return this.hstRepo.getElementVersionAtDate(tkE.getHaltestelle1Id(), tkVersion.getGueltigBis());
    }


    @Override
    public HaltestelleVersion getEndHaltestelleVersion(TarifkanteVersion tkVersion) {
        var tkE = this.getElement(tkVersion.getElementId());

        return this.hstRepo.getElementVersionAtDate(tkE.getHaltestelle2Id(), tkVersion.getGueltigBis());
    }


    @Override
    public Epsg3857Coordinate getStartCoordinate(TarifkanteVersion tkVersion) {
        var hst1V = this.getStartHaltestelleVersion(tkVersion);

        return hst1V.getCoordinate();
    }


    @Override
    public Epsg3857Coordinate getEndCoordinate(TarifkanteVersion tkVersion) {
        var hst2V = this.getEndHaltestelleVersion(tkVersion);

        return hst2V.getCoordinate();
    }


    @Override
    public boolean hasOneOfVerwaltungAndVmTypes(
        TarifkanteVersion tkVersion,
        Collection<VerkehrsmittelTyp> vmTypes,
        Map<Long, Long> verwaltungIdMap
    ) {
        return this.getVerkehrskanteVersions(tkVersion)
            .stream()
            .anyMatch(vkV -> vkV.hasOneOfVerwaltungAndVmTypes(vmTypes, verwaltungIdMap));
    }


    private List<VerkehrskanteVersion> getVerkehrskanteVersions(TarifkanteVersion tkVersion) {
        return tkVersion.getVerkehrskanteIds()
            .stream()
            .map(vkId -> this.verkehrskanteRepo.getElementVersionAtDate(vkId, tkVersion.getGueltigBis()))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }


    private AreaQuadTree<TarifkanteVersion> createQuadTree(VersionedObjectMap<Tarifkante, TarifkanteVersion> versionedObjectMap) {
        AreaQuadTree<TarifkanteVersion> versionQuadTree = new AreaQuadTree<>(
            QuadTreeConfig.MAX_TREE_DEPTH,
            new QuadTreeExtent(
                new QuadTreeCoordinate(QuadTreeConfig.MIN_COORD_X, QuadTreeConfig.MIN_COORD_Y),
                new QuadTreeCoordinate(QuadTreeConfig.MAX_COORD_X, QuadTreeConfig.MAX_COORD_Y)
            )
        );

        versionedObjectMap.getAllVersions()
            .forEach(tkV -> {
                var item = this.createQuadTreeItem(tkV);
                if (item != null) {
                    versionQuadTree.addItem(item);
                } else {
                    logger.warn(String.format("missing haltestelle version for TK version %s", tkV.getId()));
                }
            });

        return versionQuadTree;
    }


    private QuadTreeExtent getQuadTreeExtent(Epsg3857Coordinate startCoordinate, Epsg3857Coordinate endCoordinate) {
        var minCoord = new QuadTreeCoordinate(
            Math.min(startCoordinate.getE(), endCoordinate.getE()),
            Math.min(startCoordinate.getN(), endCoordinate.getN())
        );
        var maxCoord = new QuadTreeCoordinate(
            Math.max(startCoordinate.getE(), endCoordinate.getE()),
            Math.max(startCoordinate.getN(), endCoordinate.getN())
        );

        return new QuadTreeExtent(minCoord, maxCoord);
    }


    private AreaQuadTreeItem<TarifkanteVersion> createQuadTreeItem(TarifkanteVersion tkV) {
        var hst1V = this.getStartHaltestelleVersion(tkV);
        var hst2V = this.getEndHaltestelleVersion(tkV);
        if (hst1V != null && hst2V != null) {
            return new AreaQuadTreeItem<>(this.getQuadTreeExtent(hst1V.getCoordinate(), hst2V.getCoordinate()), tkV);
        } else {
            return null;
        }
    }


    @SneakyThrows
    @Synchronized
    private void updateWhenChanged() {
        if (this.debounceTimer.isInDebounceTime()) {
            return;
        }

        var changes = this.tkPersistenceRepo.findChanges(
            this.debounceTimer.getPreviousChangeCheck(),
            this.versionedObjectMap.getAllVersionKeys()
        );

        this.versionedObjectMap.updateChanges(changes);

        if (!changes.getModifiedVersions().isEmpty()) {
            changes.getModifiedVersions().forEach(tkV -> {
                this.versionQuadTree.removeItem(tkV.getId());
                var item = this.createQuadTreeItem(tkV);
                if (item != null) {
                    this.versionQuadTree.addItem(item);
                }
            });

            this.tkUpdateSubject.onNext(true);
        }

        if (!changes.getDeletedVersionIds().isEmpty()) {
            changes.getDeletedVersionIds().forEach(vId -> this.versionQuadTree.removeItem(vId));

            this.tkUpdateSubject.onNext(true);
        }
    }
}
