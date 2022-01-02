package com.tschanz.geobooster.netz_cache.service;


import com.tschanz.geobooster.geofeature.model.Coordinate;
import com.tschanz.geobooster.geofeature.model.Epsg4326Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz.model.*;
import com.tschanz.geobooster.netz.service.HaltestelleRepo;
import com.tschanz.geobooster.netz.service.VerkehrskantePersistenceRepo;
import com.tschanz.geobooster.netz.service.VerkehrskanteRepo;
import com.tschanz.geobooster.netz.service.VerwaltungRepo;
import com.tschanz.geobooster.quadtree.model.AreaQuadTree;
import com.tschanz.geobooster.quadtree.model.AreaQuadTreeItem;
import com.tschanz.geobooster.quadtree.model.QuadTreeCoordinate;
import com.tschanz.geobooster.quadtree.model.QuadTreeExtent;
import com.tschanz.geobooster.state.ProgressState;
import com.tschanz.geobooster.state_netz.VerkehrskanteState;
import com.tschanz.geobooster.versioning.model.VersionedObjectMap;
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
public class VerkehrskanteCacheRepo implements VerkehrskanteRepo {
    private static final Logger logger = LogManager.getLogger(VerkehrskanteCacheRepo.class);
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

    private final VerkehrskantePersistenceRepo vkPersistenceRepo;
    private final HaltestelleRepo hstRepo;
    private final VerwaltungRepo verwaltungRepo;
    private final ProgressState progressState;
    private final VerkehrskanteState verkehrskanteState;

    private VersionedObjectMap<Verkehrskante, VerkehrskanteVersion> versionedObjectMap;
    private AreaQuadTree<VerkehrskanteVersion> versionQuadTree;


    @Override
    public void loadAll() {
        this.verkehrskanteState.updateIsLoading(true);

        this.progressState.updateProgressText("loading verkehrskanten...");
        var elements = this.vkPersistenceRepo.readAllElements();
        this.verkehrskanteState.updateLoadedElementCount(elements.size());

        this.progressState.updateProgressText("loading verkehrskante versions...");
        var versions = this.vkPersistenceRepo.readAllVersions();
        this.verkehrskanteState.updateLoadedVersionCount(versions.size());

        this.progressState.updateProgressText("initializing verkehrskante repo...");
        this.versionedObjectMap = new VersionedObjectMap<>(elements, versions);

        this.versionQuadTree = new AreaQuadTree<>(
            MAX_TREE_DEPTH,
            new QuadTreeExtent(
                new QuadTreeCoordinate(MIN_COORD_X, MIN_COORD_Y),
                new QuadTreeCoordinate(MAX_COORD_X, MAX_COORD_Y)
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

        this.progressState.updateProgressText("loading verkehrskante done");
        this.verkehrskanteState.updateIsLoading(false);
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
    public List<VerkehrskanteVersion> searchVersions(
        LocalDate date,
        Extent extent,
        List<VerkehrsmittelTyp> vmTypes,
        List<Long> verwaltungVersionIds,
        boolean showTerminiert
    ) {
        var verwaltungIds = verwaltungVersionIds.stream()
            .map(verwVId -> this.verwaltungRepo.getVersion(verwVId).getElementId())
            .collect(Collectors.toList());

        var vkvs = this.versionQuadTree
            .findItems(this.getQuadTreeExtent(extent.getMinCoordinate(), extent.getMaxCoordinate()))
            .stream()
            .map(AreaQuadTreeItem::getItem)
            .collect(Collectors.toList());

        var vkeIds = vkvs.stream().map(VerkehrskanteVersion::getElementId).collect(Collectors.toList());
        var kiesenUttigen = vkeIds.contains(1646591L);
        var wichtrachKiesen = vkeIds.contains(1652195L);
        var bernMuensingen = vkeIds.contains(1652027L);

        return this.versionQuadTree
            .findItems(this.getQuadTreeExtent(extent.getMinCoordinate(), extent.getMaxCoordinate()))
            .stream()
            .map(AreaQuadTreeItem::getItem)
            .filter(vkV -> date.isAfter(vkV.getGueltigVon()) || date.isEqual(vkV.getGueltigVon()))
            .filter(vkV -> date.isBefore(vkV.getGueltigBis()) || date.isEqual(vkV.getGueltigBis()))
            .filter(vkV -> vmTypes.isEmpty() || vkV.hasOneOfVmTypes(vmTypes))
            .filter(vkV -> verwaltungIds.isEmpty() || vkV.hasOneOfVerwaltungIds(verwaltungIds))
            .filter(vkV -> showTerminiert || vkV.getTerminiertPer() == null || vkV.getTerminiertPer().isAfter(date))
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
    public Epsg4326Coordinate getStartCoordinate(VerkehrskanteVersion vkVersion) {
        var hst1V = this.getStartHaltestelleVersion(vkVersion);

        return hst1V.getCoordinate();
    }


    @Override
    public Epsg4326Coordinate getEndCoordinate(VerkehrskanteVersion vkVersion) {
        var hst2V = this.getEndHaltestelleVersion(vkVersion);

        return hst2V.getCoordinate();
    }


    @Override
    public VersionedObjectMap<Verkehrskante, VerkehrskanteVersion> getVersionedObjectMap() {
        return this.versionedObjectMap;
    }


    private QuadTreeExtent getQuadTreeExtent(Coordinate startCoordinate, Coordinate endCoordinate) {
        var startCoord = CoordinateConverter.convertToEpsg3857(startCoordinate);
        var endCoord = CoordinateConverter.convertToEpsg3857(endCoordinate);
        var minCoord = new QuadTreeCoordinate(
            Math.min(startCoord.getE(), endCoord.getE()),
            Math.min(startCoord.getN(), endCoord.getN())
        );
        var maxCoord = new QuadTreeCoordinate(
            Math.max(startCoord.getE(), endCoord.getE()),
            Math.max(startCoord.getN(), endCoord.getN())
        );

        return new QuadTreeExtent(minCoord, maxCoord);
    }
}
