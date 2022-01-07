package com.tschanz.geobooster.rtm_repo.service;


import com.tschanz.geobooster.geofeature.model.Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz_repo.model.ProgressState;
import com.tschanz.geobooster.netz_repo.model.QuadTreeConfig;
import com.tschanz.geobooster.netz_repo.service.HaltestelleRepo;
import com.tschanz.geobooster.quadtree.model.QuadTree;
import com.tschanz.geobooster.quadtree.model.QuadTreeCoordinate;
import com.tschanz.geobooster.quadtree.model.QuadTreeExtent;
import com.tschanz.geobooster.quadtree.model.QuadTreeItem;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabe;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabeVersion;
import com.tschanz.geobooster.rtm_persistence.service.HaltestelleWegangabePersistence;
import com.tschanz.geobooster.rtm_repo.model.HaltestelleWegangabeRepoState;
import com.tschanz.geobooster.versioning_repo.model.VersionedObjectMap;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class HaltestelleWegangabeRepoImpl implements HaltestelleWegangabeRepo {
    private static final Logger logger = LogManager.getLogger(HaltestelleWegangabeRepoImpl.class);

    private final HaltestelleRepo hstRepo;
    private final HaltestelleWegangabePersistence hstWegangabePersistence;
    private final ProgressState progressState;
    private final HaltestelleWegangabeRepoState hstWegangabeRepoState;

    private VersionedObjectMap<HaltestelleWegangabe, HaltestelleWegangabeVersion> versionedObjectMap;
    private QuadTree<HaltestelleWegangabeVersion> versionQuadTree;


    @Override
    public void loadAll() {
        this.hstWegangabeRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("loading haltestelle wegangaben...");
        var elements = this.hstWegangabePersistence.readAllElements();
        this.hstWegangabeRepoState.updateLoadedElementCount(elements.size());

        this.progressState.updateProgressText("loading haltestelle wegangabe versions...");
        var versions = this.hstWegangabePersistence.readAllVersions();
        this.hstWegangabeRepoState.updateLoadedVersionCount(versions.size());

        this.progressState.updateProgressText("initializing haltestelle wegangaben repo...");
        this.versionedObjectMap = new VersionedObjectMap<>(elements, versions);

        this.versionQuadTree = new QuadTree<>(
            QuadTreeConfig.MAX_TREE_DEPTH,
            new QuadTreeExtent(
                new QuadTreeCoordinate(QuadTreeConfig.MIN_COORD_X, QuadTreeConfig.MIN_COORD_Y),
                new QuadTreeCoordinate(QuadTreeConfig.MAX_COORD_X, QuadTreeConfig.MAX_COORD_Y)
            )
        );

        this.versionedObjectMap.getAllVersions()
            .forEach(hstWegangabeV -> {
                var hstV = this.getHaltestelleVersion(hstWegangabeV);
                if (hstV != null) {
                    var coord = hstV.getCoordinate();
                    this.versionQuadTree.addItem(
                        new QuadTreeItem<>(this.getQuadTreeCoordinates(coord), hstWegangabeV)
                    );
                } else {
                    logger.warn(String.format("missing haltestelle for HST Wegangabe version %s", hstWegangabeV.getId()));
                }
            });

        this.progressState.updateProgressText("loading haltestelle wegangaben done");
        this.hstWegangabeRepoState.updateIsLoading(false);
    }


    @Override
    public List<HaltestelleWegangabeVersion> searchByExtent(Extent extent) {
        return this.versionQuadTree.findItems(this.getQuadTreeExtent(extent)).stream()
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
    public HaltestelleWegangabe getElement(long id) {
        return this.versionedObjectMap.getElement(id);
    }


    @Override
    public HaltestelleWegangabeVersion getVersion(long id) {
        return this.versionedObjectMap.getVersion(id);
    }


    @Override
    public Collection<HaltestelleWegangabeVersion> getElementVersions(long elementId) {
        return this.versionedObjectMap.getElementVersions(elementId);
    }


    @Override
    public HaltestelleWegangabeVersion getElementVersionAtDate(long elementId, LocalDate date) {
        return this.versionedObjectMap.getElementVersionAtDate(elementId, date);
    }


    private QuadTreeExtent getQuadTreeExtent(Extent extent) {
        return new QuadTreeExtent(
            this.getQuadTreeCoordinates(extent.getMinCoordinate()),
            this.getQuadTreeCoordinates(extent.getMaxCoordinate())
        );
    }


    private QuadTreeCoordinate getQuadTreeCoordinates(Coordinate coordinate) {
        var coord = CoordinateConverter.convertToEpsg3857(coordinate);

        return new QuadTreeCoordinate(coord.getE(), coord.getN());
    }
}
