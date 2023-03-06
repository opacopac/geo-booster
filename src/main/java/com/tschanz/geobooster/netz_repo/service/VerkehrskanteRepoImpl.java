package com.tschanz.geobooster.netz_repo.service;


import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz.model.Verkehrskante;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz_persistence.service.VerkehrskantePersistence;
import com.tschanz.geobooster.netz_repo.model.ProgressState;
import com.tschanz.geobooster.netz_repo.model.QuadTreeConfig;
import com.tschanz.geobooster.netz_repo.model.VerkehrskanteRepoState;
import com.tschanz.geobooster.quadtree.model.AreaQuadTree;
import com.tschanz.geobooster.quadtree.model.AreaQuadTreeItem;
import com.tschanz.geobooster.quadtree.model.QuadTreeCoordinate;
import com.tschanz.geobooster.quadtree.model.QuadTreeExtent;
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
public class VerkehrskanteRepoImpl implements VerkehrskanteRepo {
    private static final Logger logger = LogManager.getLogger(VerkehrskanteRepoImpl.class);

    private final VerkehrskantePersistence vkPersistenceRepo;
    private final HaltestelleRepo hstRepo;
    private final ProgressState progressState;
    private final VerkehrskanteRepoState verkehrskanteRepoState;

    private Collection<Verkehrskante> elements;
    private Collection<VerkehrskanteVersion> versions;
    private VersionedObjectMap<Verkehrskante, VerkehrskanteVersion> versionedObjectMap;
    private AreaQuadTree<VerkehrskanteVersion> versionQuadTree;


    @Override
    public void loadData() {
        this.verkehrskanteRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("loading verkehrskanten...");
        this.elements = this.vkPersistenceRepo.readAllElements();
        this.verkehrskanteRepoState.updateLoadedElementCount(this.elements.size());

        this.progressState.updateProgressText("loading verkehrskante versions...");
        this.versions = this.vkPersistenceRepo.readAllVersions();
        this.verkehrskanteRepoState.updateLoadedVersionCount(this.versions.size());

        this.progressState.updateProgressText("loading verkehrskante done");
        this.verkehrskanteRepoState.updateIsLoading(false);
    }


    @Override
    public void initRepo() {
        this.verkehrskanteRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("initializing verkehrskante repo...");
        this.versionedObjectMap = new VersionedObjectMap<>(this.elements, this.versions);

        this.versionQuadTree = new AreaQuadTree<>(
            QuadTreeConfig.MAX_TREE_DEPTH,
            new QuadTreeExtent(
                new QuadTreeCoordinate(QuadTreeConfig.MIN_COORD_X, QuadTreeConfig.MIN_COORD_Y),
                new QuadTreeCoordinate(QuadTreeConfig.MAX_COORD_X, QuadTreeConfig.MAX_COORD_Y)
            )
        );

        this.versionedObjectMap.getAllVersions()
            .forEach(vkV -> {
                var hst1V = this.getStartHaltestelleVersion(vkV);
                var hst2V = this.getEndHaltestelleVersion(vkV);
                if (hst1V != null && hst2V != null) {
                    this.versionQuadTree.addItem(
                            new AreaQuadTreeItem<>(this.getQuadTreeExtent(hst1V.getCoordinate(), hst2V.getCoordinate()), vkV)
                    );
                } else {
                    logger.warn(String.format("missing haltestelle version for VK version %s", vkV.getId()));
                }
            });

        this.elements.clear();
        this.versions.clear();

        this.progressState.updateProgressText("initializing verkehrskante done");
        this.verkehrskanteRepoState.updateIsLoading(false);
    }


    @Override
    public Verkehrskante getElement(long id) {
        return this.versionedObjectMap.getElement(id);
    }


    @Override
    public VerkehrskanteVersion getVersion(long id) {
        return this.versionedObjectMap.getVersion(id);
    }


    @Override
    public Collection<VerkehrskanteVersion> getElementVersions(long elementId) {
        return this.versionedObjectMap.getElementVersions(elementId);
    }


    @Override
    public VerkehrskanteVersion getElementVersionAtDate(long elementId, LocalDate date) {
        return this.versionedObjectMap.getElementVersionAtDate(elementId, date);
    }


    @Override
    public List<VerkehrskanteVersion> searchByExtent(Extent<Epsg3857Coordinate> extent) {
        return this.versionQuadTree
            .findItems(this.getQuadTreeExtent(extent.getMinCoordinate(), extent.getMaxCoordinate()))
            .stream()
            .map(AreaQuadTreeItem::getItem)
            .collect(Collectors.toList());
    }


    @Override
    public Haltestelle getStartHaltestelle(VerkehrskanteVersion vkVersion) {
        var vkE = this.getElement(vkVersion.getElementId());

        return this.hstRepo.getElement(vkE.getHaltestelle1Id());
    }


    @Override
    public Haltestelle getEndHaltestelle(VerkehrskanteVersion vkVersion) {
        var vkE = this.getElement(vkVersion.getElementId());

        return this.hstRepo.getElement(vkE.getHaltestelle2Id());
    }


    @Override
    public HaltestelleVersion getStartHaltestelleVersion(VerkehrskanteVersion vkVersion) {
        var vkE = this.getElement(vkVersion.getElementId());

        return this.hstRepo.getElementVersionAtDate(vkE.getHaltestelle1Id(), vkVersion.getGueltigBis());
    }


    @Override
    public HaltestelleVersion getEndHaltestelleVersion(VerkehrskanteVersion vkVersion) {
        var vkE = this.getElement(vkVersion.getElementId());

        return this.hstRepo.getElementVersionAtDate(vkE.getHaltestelle2Id(), vkVersion.getGueltigBis());
    }


    @Override
    public Epsg3857Coordinate getStartCoordinate(VerkehrskanteVersion vkVersion) {
        var hst1V = this.getStartHaltestelleVersion(vkVersion);

        return hst1V.getCoordinate();
    }


    @Override
    public Epsg3857Coordinate getEndCoordinate(VerkehrskanteVersion vkVersion) {
        var hst2V = this.getEndHaltestelleVersion(vkVersion);

        return hst2V.getCoordinate();
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
}
