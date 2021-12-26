package com.tschanz.geobooster.netz_cache.service;


import com.tschanz.geobooster.geofeature.model.Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz_persistence.service.HaltestellenPersistenceRepo;
import com.tschanz.geobooster.quadtree.model.QuadTree;
import com.tschanz.geobooster.quadtree.model.QuadTreeCoordinate;
import com.tschanz.geobooster.quadtree.model.QuadTreeExtent;
import com.tschanz.geobooster.quadtree.model.QuadTreeItem;
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
public class HaltestelleCacheRepoImpl implements HaltestelleCacheRepo {
    private static final Logger logger = LogManager.getLogger(HaltestelleCacheRepoImpl.class);
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

    private final HaltestellenPersistenceRepo haltestellenPersistenceRepo;
    private Map<Long, Haltestelle> elementMap;
    private Map<Long, HaltestelleVersion> versionMap;
    private QuadTree<HaltestelleVersion> versionQuadTree;


    @Override
    public void init() {
        this.getElementMap();
        this.getVersionMap();
        this.getVersionQuadTree();
    }


    public Map<Long, Haltestelle> getElementMap() {
        if (this.elementMap == null) {
            logger.info("loading all hst elements...");
            this.elementMap = this.haltestellenPersistenceRepo.readAllElements();
            logger.info(String.format("cached %d hst elements.", this.elementMap.size()));
        }

        return this.elementMap;
    }


    public Map<Long, HaltestelleVersion> getVersionMap() {
        if (this.versionMap == null) {
            logger.info("loading all hst versions...");
            this.versionMap = this.haltestellenPersistenceRepo.readAllVersions(this.getElementMap());
            logger.info(String.format("cached %d hst versions.", this.versionMap.size()));
        }

        return this.versionMap;
    }


    public QuadTree<HaltestelleVersion> getVersionQuadTree() {
        if (this.versionQuadTree == null) {
            // init quad tree
            this.versionQuadTree = new QuadTree<>(
                MAX_TREE_DEPTH,
                new QuadTreeExtent(
                    new QuadTreeCoordinate(MIN_COORD_X, MIN_COORD_Y),
                    new QuadTreeCoordinate(MAX_COORD_X, MAX_COORD_Y)
                )
            );

            // populate quad tree
            this.getVersionMap().values()
                .forEach(hstV -> {
                    this.versionQuadTree.addItem(
                        new QuadTreeItem<>(this.getQuadTreeCoordinates(hstV.getCoordinate()), hstV)
                    );
                });
        }

        return this.versionQuadTree;
    }


    @Override
    public List<HaltestelleVersion> readVersions(LocalDate date, Extent extent) {
        return this.getVersionQuadTree().findItems(this.getQuadTreeExtent(extent)).stream()
            .map(QuadTreeItem::getItem)
            .filter(hstv -> date.isEqual(hstv.getVersionInfo().getGueltigVon()) || date.isAfter(hstv.getVersionInfo().getGueltigVon()))
            .filter(hstv -> date.isEqual(hstv.getVersionInfo().getGueltigBis()) || date.isBefore(hstv.getVersionInfo().getGueltigBis()))
            .collect(Collectors.toList());
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
}
