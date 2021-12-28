package com.tschanz.geobooster.netz_cache.service;


import com.tschanz.geobooster.geofeature.model.Coordinate;
import com.tschanz.geobooster.geofeature.model.Epsg4326Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz.model.*;
import com.tschanz.geobooster.netz.service.HaltestelleRepo;
import com.tschanz.geobooster.netz.service.VerkehrskanteRepo;
import com.tschanz.geobooster.quadtree.model.AreaQuadTree;
import com.tschanz.geobooster.quadtree.model.AreaQuadTreeItem;
import com.tschanz.geobooster.quadtree.model.QuadTreeCoordinate;
import com.tschanz.geobooster.quadtree.model.QuadTreeExtent;
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

    private final HaltestelleRepo hstRepo;
    private VersionedObjectMap<Verkehrskante, VerkehrskanteVersion> versionedObjectMap;
    private AreaQuadTree<VerkehrskanteVersion> versionQuadTree;


    @Override
    public void init(Collection<Verkehrskante> elements, Collection<VerkehrskanteVersion> versions) {
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
    public List<VerkehrskanteVersion> readVersions(LocalDate date, Extent extent, List<VerkehrsmittelTyp> vmTypes) {
        return this.versionQuadTree
            .findItems(this.getQuadTreeExtent(extent.getMinCoordinate(), extent.getMaxCoordinate()))
            .stream()
            .map(AreaQuadTreeItem::getItem)
            .filter(vkV -> date.isEqual(vkV.getGueltigVon()) || date.isAfter(vkV.getGueltigVon()))
            .filter(vkV -> date.isEqual(vkV.getGueltigBis()) || date.isBefore(vkV.getGueltigBis()))
            .filter(vkV -> vkV.hasOneOfVmTypes(vmTypes))
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

        return this.hstRepo.getElementVersionAtDate(vkE.getHaltestelle1Id(), vkVersion.getGueltigVon());
    }


    @Override
    public HaltestelleVersion getEndHaltestelleVersion(VerkehrskanteVersion vkVersion) {
        var vkE = this.getElement(vkVersion.getElementId());

        return this.hstRepo.getElementVersionAtDate(vkE.getHaltestelle2Id(), vkVersion.getGueltigVon());
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

        return new QuadTreeExtent(
            new QuadTreeCoordinate(startCoord.getE(), startCoord.getN()),
            new QuadTreeCoordinate(endCoord.getE(), endCoord.getN())
        );
    }
}
