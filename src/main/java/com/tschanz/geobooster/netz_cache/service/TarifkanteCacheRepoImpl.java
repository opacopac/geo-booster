package com.tschanz.geobooster.netz_cache.service;


import com.tschanz.geobooster.geofeature.model.Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz.model.Tarifkante;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;
import com.tschanz.geobooster.netz_persistence.service.TarifkantePersistenceRepo;
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
public class TarifkanteCacheRepoImpl implements TarifkanteCacheRepo {
    private static final Logger logger = LogManager.getLogger(TarifkanteCacheRepoImpl.class);
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

    private final TarifkantePersistenceRepo tkPersistenceRepo;
    private final HaltestelleCacheRepo hstRepo;
    private final VerkehrskanteCacheRepo vkRepo;
    private Map<Long, Tarifkante> elementMap;
    private Map<Long, TarifkanteVersion> versionMap;
    private AreaQuadTree<TarifkanteVersion> versionQuadTree;


    @Override
    public void init() {
        this.getElementMap();
        this.getVersionMap();
        this.getVersionQuadTree();
    }


    public Map<Long, Tarifkante> getElementMap() {
        if (this.elementMap == null) {
            logger.info("loading all tk elements...");
            this.elementMap = this.tkPersistenceRepo.readAllElements(this.hstRepo.getElementMap());
            logger.info(String.format("cached %d tk elements.", this.elementMap.size()));
        }

        return this.elementMap;
    }


    public Map<Long, TarifkanteVersion> getVersionMap() {
        if (this.versionMap == null) {
            logger.info("loading all tk versions...");
            this.versionMap = this.tkPersistenceRepo.readAllVersions(this.getElementMap(), this.vkRepo.getElementMap());
            logger.info(String.format("cached %d tk versions.", this.versionMap.size()));
        }

        return this.versionMap;
    }


    public AreaQuadTree<TarifkanteVersion> getVersionQuadTree() {
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
                .forEach(tkV -> {
                    if (tkV.getHaltestelle1Version() != null && tkV.getHaltestelle2Version() != null) {
                        this.versionQuadTree.addItem(
                            new AreaQuadTreeItem<>(this.getQuadTreeExtent(tkV.getStartCoordinate(), tkV.getEndCoordinate()), tkV)
                        );
                    } else {
                        logger.warn(String.format("missing haltestelle version for TK version %s", tkV.getVersionInfo().getId()));
                    }
                });
        }

        return this.versionQuadTree;
    }


    @Override
    public List<TarifkanteVersion> readVersions(LocalDate date, Extent extent, List<VerkehrsmittelTyp> vmTypes) {
        return this.getVersionQuadTree()
            .findItems(this.getQuadTreeExtent(extent.getMinCoordinate(), extent.getMaxCoordinate()))
            .stream()
            .map(AreaQuadTreeItem::getItem)
            .filter(tkV -> date.isEqual(tkV.getVersionInfo().getGueltigVon()) || date.isAfter(tkV.getVersionInfo().getGueltigVon()))
            .filter(tkV -> date.isEqual(tkV.getVersionInfo().getGueltigBis()) || date.isBefore(tkV.getVersionInfo().getGueltigBis()))
            .filter(tkV -> tkV.hasOneOfVmTypes(vmTypes))
            .collect(Collectors.toList());
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
