package com.tschanz.geobooster.netz_cache.service;


import com.tschanz.geobooster.geofeature.model.Coordinate;
import com.tschanz.geobooster.geofeature.model.Epsg4326Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz.model.*;
import com.tschanz.geobooster.netz.service.HaltestelleRepo;
import com.tschanz.geobooster.netz.service.TarifkanteRepo;
import com.tschanz.geobooster.netz.service.VerkehrskanteRepo;
import com.tschanz.geobooster.netz.service.VerwaltungRepo;
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
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TarifkanteCacheRepo implements TarifkanteRepo {
    private static final Logger logger = LogManager.getLogger(TarifkanteCacheRepo.class);

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

    private final VerwaltungRepo verwaltungRepo;
    private final HaltestelleRepo hstRepo;
    private final VerkehrskanteRepo vkRepo;
    private VersionedObjectMap<Tarifkante, TarifkanteVersion> versionedObjectMap;
    private AreaQuadTree<TarifkanteVersion> versionQuadTree;


    @Override
    public void init(Collection<Tarifkante> elements, Collection<TarifkanteVersion> versions) {
        this.versionedObjectMap = new VersionedObjectMap<>(elements, versions);

        this.versionQuadTree = new AreaQuadTree<>(
            MAX_TREE_DEPTH,
            new QuadTreeExtent(
                new QuadTreeCoordinate(MIN_COORD_X, MIN_COORD_Y),
                new QuadTreeCoordinate(MAX_COORD_X, MAX_COORD_Y)
            )
        );

        this.versionedObjectMap.getAllVersions()
            .forEach(tkV -> {
                var hst1V = this.getStartHaltestelleVersion(tkV);
                var hst2V = this.getEndHaltestelleVersion(tkV);
                if (hst1V != null && hst2V != null) {
                    this.versionQuadTree.addItem(
                        new AreaQuadTreeItem<>(this.getQuadTreeExtent(hst1V.getCoordinate(), hst2V.getCoordinate()), tkV)
                    );
                } else {
                    logger.warn(String.format("missing haltestelle version for TK version %s", tkV.getId()));
                }
            });
    }


    @Override
    public Tarifkante getElement(long id) {
        return this.versionedObjectMap.getElement(id);
    }


    @Override
    public TarifkanteVersion getVersion(long id) {
        return this.versionedObjectMap.getVersion(id);
    }


    @Override
    public Collection<TarifkanteVersion> getElementVersions(long elementId) {
        return this.versionedObjectMap.getElementVersions(elementId);
    }


    @Override
    public TarifkanteVersion getElementVersionAtDate(long elementId, LocalDate date) {
        return this.versionedObjectMap.getElementVersionAtDate(elementId, date);
    }


    @Override
    public List<TarifkanteVersion> searchVersions(LocalDate date, Extent extent, List<VerkehrsmittelTyp> vmTypes, List<Long> verwaltungVersionIds) {
        var verwaltungIds = verwaltungVersionIds.stream()
            .map(verwVId -> this.verwaltungRepo.getVersion(verwVId).getElementId())
            .collect(Collectors.toList());

        return this.versionQuadTree
            .findItems(this.getQuadTreeExtent(extent.getMinCoordinate(), extent.getMaxCoordinate()))
            .stream()
            .map(AreaQuadTreeItem::getItem)
            .filter(tkV -> date.isEqual(tkV.getGueltigVon()) || date.isAfter(tkV.getGueltigVon()))
            .filter(tkV -> date.isEqual(tkV.getGueltigBis()) || date.isBefore(tkV.getGueltigBis()))
            .filter(tkV -> vmTypes.isEmpty() || this.hasOneOfVmTypes(tkV, vmTypes))
            .filter(tkV -> verwaltungIds.isEmpty() || this.hasOneOfVerwaltungIds(tkV, verwaltungIds))
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
    public Epsg4326Coordinate getStartCoordinate(TarifkanteVersion tkVersion) {
        var hst1V = this.getStartHaltestelleVersion(tkVersion);

        return hst1V.getCoordinate();
    }


    @Override
    public Epsg4326Coordinate getEndCoordinate(TarifkanteVersion tkVersion) {
        var hst2V = this.getEndHaltestelleVersion(tkVersion);

        return hst2V.getCoordinate();
    }


    @Override
    public VersionedObjectMap<Tarifkante, TarifkanteVersion> getVersionedObjectMap() {
        return this.versionedObjectMap;
    }


    private List<VerkehrskanteVersion> getVerkehrskanteVersions(TarifkanteVersion tkVersion) {
        return tkVersion.getVerkehrskanteIds()
            .stream()
            .map(vkId -> this.vkRepo.getElementVersionAtDate(vkId, tkVersion.getGueltigBis()))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }


    private boolean hasOneOfVmTypes(TarifkanteVersion tkVersion, List<VerkehrsmittelTyp> vmTypes) {
        var tkVmTypes = this.getVerkehrskanteVersions(tkVersion)
            .stream()
            .flatMap(vkv -> vkv.getVmTypes().stream())
            .collect(Collectors.toList());

        var vmTypeBitmask = VerkehrsmittelTyp.getBitMask(tkVmTypes);
        return (VerkehrsmittelTyp.getBitMask(vmTypes) & vmTypeBitmask) > 0;
    }


    private boolean hasOneOfVerwaltungIds(TarifkanteVersion tkVersion, List<Long> verwaltungIds) {
        var tkVerwaltungen = this.getVerkehrskanteVersions(tkVersion)
            .stream()
            .flatMap(vkv -> vkv.getVerwaltungIds().stream())
            .collect(Collectors.toList());

        for (var verwaltungId: verwaltungIds) {
            if (tkVerwaltungen.contains(verwaltungId)) {
                return true;
            }
        };

        return false;
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
