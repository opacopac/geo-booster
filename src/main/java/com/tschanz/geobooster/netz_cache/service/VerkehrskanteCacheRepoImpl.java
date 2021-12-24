package com.tschanz.geobooster.netz_cache.service;


import com.tschanz.geobooster.geofeature.model.Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz.model.Verkehrskante;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;
import com.tschanz.geobooster.netz_persistence.service.VerkehrskantePersistenceRepo;
import com.tschanz.geobooster.quadtree.model.AreaQuadTree;
import com.tschanz.geobooster.quadtree.model.AreaQuadTreeItem;
import com.tschanz.geobooster.quadtree.model.QuadTreeCoordinate;
import com.tschanz.geobooster.quadtree.model.QuadTreeExtent;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class VerkehrskanteCacheRepoImpl implements VerkehrskanteCacheRepo {
    private static final Logger logger = LogManager.getLogger(VerkehrskanteCacheRepoImpl.class);
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

    private final HaltestelleCacheRepo hstRepo;
    private final VerwaltungCacheRepo verwaltungRepo;
    private final VerkehrskantePersistenceRepo vkPersistenceRepo;
    private Map<Long, Verkehrskante> elementMap;
    private Map<Long, VerkehrskanteVersion> versionMap;
    private AreaQuadTree<VerkehrskanteVersion> versionQuadTree;


    public Map<Long, Verkehrskante> getElementMap() {
        if (this.elementMap == null) {
            logger.info("loading all vk elements...");
            this.elementMap = this.vkPersistenceRepo.readAllElements(this.hstRepo.getElementMap());
            logger.info(String.format("cached %d vk elements.", this.elementMap.size()));
        }

        return this.elementMap;
    }


    public Map<Long, VerkehrskanteVersion> getVersionMap() {
        if (this.versionMap == null) {
            logger.info("loading all vk versions...");
            this.versionMap = this.vkPersistenceRepo.readAllVersions(this.getElementMap(), this.verwaltungRepo.getElementMap());
            logger.info(String.format("cached %d vk versions.", this.versionMap.size()));
        }

        return this.versionMap;
    }


    public AreaQuadTree<VerkehrskanteVersion> getVersionQuadTree() {
        if (this.versionQuadTree == null) {
            // init quad tree
            this.versionQuadTree = new AreaQuadTree<>(
                MAX_TREE_DEPTH,
                new QuadTreeExtent(
                    new QuadTreeCoordinate(MIN_COORD_X, MIN_COORD_Y),
                    new QuadTreeCoordinate(MAX_COORD_X, MAX_COORD_Y)
                )
            );

            // populate quad tree
            this.getVersionMap().values()
                .forEach(vkV -> {
                    if (vkV.getHaltestelle1Version() != null && vkV.getHaltestelle2Version() != null) {
                        this.versionQuadTree.addItem(
                            new AreaQuadTreeItem<>(this.getQuadTreeExtent(vkV.getStartCoordinate(), vkV.getEndCoordinate()), vkV)
                        );
                    } else {
                        logger.warn(String.format("missing haltestelle version for VK version %s", vkV.getVersionInfo().getId()));
                    }
                });
        }

        return this.versionQuadTree;
    }


    @Override
    public List<VerkehrskanteVersion> readVersions(LocalDate date, Extent extent, List<VerkehrsmittelTyp> vmTypes) {
        return this.getVersionQuadTree()
            .findItems(this.getQuadTreeExtent(extent.getMinCoordinate(), extent.getMaxCoordinate()))
            .stream()
            .map(AreaQuadTreeItem::getItem)
            .filter(vkV -> date.isEqual(vkV.getVersionInfo().getGueltigVon()) || date.isAfter(vkV.getVersionInfo().getGueltigVon()))
            .filter(vkV -> date.isEqual(vkV.getVersionInfo().getGueltigBis()) || date.isBefore(vkV.getVersionInfo().getGueltigBis()))
            .filter(vkV -> vkV.hasOneOfVmTypes(vmTypes))
            .collect(Collectors.toList());


        /*var minLon = CoordinateConverter.convertToEpsg4326(extent.getMinCoordinate()).getLongitude();
        var minLat = CoordinateConverter.convertToEpsg4326(extent.getMinCoordinate()).getLatitude();
        var maxLon = CoordinateConverter.convertToEpsg4326(extent.getMaxCoordinate()).getLongitude();
        var maxLat = CoordinateConverter.convertToEpsg4326(extent.getMaxCoordinate()).getLatitude();

        return this.getVersionMap().values()
            .stream()
            .filter(vkV -> date.isEqual(vkV.getVersionInfo().getGueltigVon()) || date.isAfter(vkV.getVersionInfo().getGueltigVon()))
            .filter(vkV -> date.isEqual(vkV.getVersionInfo().getGueltigBis()) || date.isBefore(vkV.getVersionInfo().getGueltigBis()))
            .filter(vkV -> vkV.hasOneOfVmTypes(vmTypes))
            //.filter(hstv -> hstv.getLng() >= minLon && hstv.getLng() <= maxLon)
            //.filter(hstv -> hstv.getLat() >= minLat && hstv.getLat() <= maxLat)
            .collect(Collectors.toList());*/
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
