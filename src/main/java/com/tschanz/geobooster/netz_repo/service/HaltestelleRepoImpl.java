package com.tschanz.geobooster.netz_repo.service;


import com.tschanz.geobooster.geofeature.model.Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz_persistence.service.HaltestellenPersistence;
import com.tschanz.geobooster.netz_repo.model.HaltestelleRepoState;
import com.tschanz.geobooster.quadtree.model.QuadTree;
import com.tschanz.geobooster.quadtree.model.QuadTreeCoordinate;
import com.tschanz.geobooster.quadtree.model.QuadTreeExtent;
import com.tschanz.geobooster.quadtree.model.QuadTreeItem;
import com.tschanz.geobooster.state.ProgressState;
import com.tschanz.geobooster.versioning_repo.model.VersionedObjectMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class HaltestelleRepoImpl implements HaltestelleRepo {
    // TODO
    // minLat = 43.0062910000
    // maxLat = 53.7988520000
    // minLon = 1x 0.6202740000, 2x 2.0794830000, rest: 4.8248030000
    // maxLon = 14.5659700000
    private static final double MIN_COORD_X = 556597.45 - 1;
    private static final double MIN_COORD_Y = 5654278.34 - 1;
    private static final double MAX_COORD_X = 1246778.30 + 1;
    private static final double MAX_COORD_Y = 6108322.79 + 1;
    private static final int MAX_TREE_DEPTH = 6;

    private final HaltestellenPersistence hstPersistenceRepo;
    private final ProgressState progressState;
    private final HaltestelleRepoState haltestelleRepoState;

    private VersionedObjectMap<Haltestelle, HaltestelleVersion> versionedObjectMap;
    private QuadTree<HaltestelleVersion> versionQuadTree;


    @Override
    public void loadAll() {
        this.haltestelleRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("loading haltestellen...");
        var elements = this.hstPersistenceRepo.readAllElements();
        this.haltestelleRepoState.updateLoadedElementCount(elements.size());

        this.progressState.updateProgressText("loading haltestelle versions...");
        var versions = this.hstPersistenceRepo.readAllVersions();
        this.haltestelleRepoState.updateLoadedVersionCount(versions.size());

        this.progressState.updateProgressText("initializing haltestelle repo...");
        this.versionedObjectMap = new VersionedObjectMap<>(elements, versions);

        this.versionQuadTree = new QuadTree<>(
            MAX_TREE_DEPTH,
            new QuadTreeExtent(
                new QuadTreeCoordinate(MIN_COORD_X, MIN_COORD_Y),
                new QuadTreeCoordinate(MAX_COORD_X, MAX_COORD_Y)
            )
        );

        this.versionedObjectMap.getAllVersions()
            .forEach(hstV -> {
                this.versionQuadTree.addItem(
                    new QuadTreeItem<>(this.getQuadTreeCoordinates(hstV.getCoordinate()), hstV)
                );
            });

        this.progressState.updateProgressText("loading haltestellen done");
        this.haltestelleRepoState.updateIsLoading(false);
    }


    @Override
    public List<HaltestelleVersion> searchVersions(LocalDate date, Extent extent) {
        return this.versionQuadTree.findItems(this.getQuadTreeExtent(extent)).stream()
            .map(QuadTreeItem::getItem)
            .filter(hstv -> date.isEqual(hstv.getGueltigVon()) || date.isAfter(hstv.getGueltigVon()))
            .filter(hstv -> date.isEqual(hstv.getGueltigBis()) || date.isBefore(hstv.getGueltigBis()))
            .collect(Collectors.toList());
    }

    @Override
    public VersionedObjectMap<Haltestelle, HaltestelleVersion> getVersionedObjectMap() {
        return this.versionedObjectMap;
    }


    private QuadTreeCoordinate getQuadTreeCoordinates(Coordinate coordinate) {
        var coord = CoordinateConverter.convertToEpsg3857(coordinate);

        return new QuadTreeCoordinate(coord.getE(), coord.getN());
    }


    private QuadTreeExtent getQuadTreeExtent(Extent extent) {
        return new QuadTreeExtent(
            this.getQuadTreeCoordinates(extent.getMinCoordinate()),
            this.getQuadTreeCoordinates(extent.getMaxCoordinate())
        );
    }


    @Override
    public Haltestelle getElement(long id) {
        return this.versionedObjectMap.getElement(id);
    }


    @Override
    public HaltestelleVersion getVersion(long id) {
        return this.versionedObjectMap.getVersion(id);
    }


    @Override
    public Collection<HaltestelleVersion> getElementVersions(long elementId) {
        return this.versionedObjectMap.getElementVersions(elementId);
    }


    @Override
    public HaltestelleVersion getElementVersionAtDate(long elementId, LocalDate date) {
        return this.versionedObjectMap.getElementVersionAtDate(elementId, date);
    }
}
