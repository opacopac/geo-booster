package com.tschanz.geobooster.netz_repo.service;


import com.tschanz.geobooster.geofeature.model.Coordinate;
import com.tschanz.geobooster.geofeature.model.Epsg4326Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz.model.*;
import com.tschanz.geobooster.netz_persistence.model.ReadFilter;
import com.tschanz.geobooster.netz_persistence.service.TarifkantePersistence;
import com.tschanz.geobooster.netz_repo.model.ProgressState;
import com.tschanz.geobooster.netz_repo.model.QuadTreeConfig;
import com.tschanz.geobooster.netz_repo.model.TarifkanteRepoState;
import com.tschanz.geobooster.persistence_sql.model.ConnectionState;
import com.tschanz.geobooster.quadtree.model.AreaQuadTree;
import com.tschanz.geobooster.quadtree.model.AreaQuadTreeItem;
import com.tschanz.geobooster.quadtree.model.QuadTreeCoordinate;
import com.tschanz.geobooster.quadtree.model.QuadTreeExtent;
import com.tschanz.geobooster.versioning_repo.model.VersionedObjectMap;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TarifkanteRepoImpl implements TarifkanteRepo {
    private static final Logger logger = LogManager.getLogger(TarifkanteRepoImpl.class);
    private static final int DEBOUNCE_TIME_LAST_CHANGE_CHECK_SEC = 5;

    private final TarifkantePersistence tkPersistenceRepo;
    private final VerwaltungRepo verwaltungRepo;
    private final HaltestelleRepo hstRepo;
    private final VerkehrskanteRepo vkRepo;
    private final ProgressState progressState;
    private final TarifkanteRepoState tarifkanteRepoState;
    private final ConnectionState connectionState;

    private VersionedObjectMap<Tarifkante, TarifkanteVersion> versionedObjectMap;
    private AreaQuadTree<TarifkanteVersion> versionQuadTree;
    private LocalDateTime lastChangeCheck = LocalDateTime.now();


    @Override
    public void loadAll() {
        this.tarifkanteRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("loading tarifkanten...");
        var elements = this.tkPersistenceRepo.readAllElements();
        this.tarifkanteRepoState.updateLoadedElementCount(elements.size());

        this.progressState.updateProgressText("loading tarifkante versions...");
        var versions = this.tkPersistenceRepo.readAllVersions();
        this.tarifkanteRepoState.updateLoadedVersionCount(versions.size());

        this.progressState.updateProgressText("initializing tarifkante repo...");
        this.versionedObjectMap = new VersionedObjectMap<>(elements, versions);
        this.versionQuadTree = this.createQuadTree(this.versionedObjectMap);

        this.lastChangeCheck = LocalDateTime.now();
        this.progressState.updateProgressText("loading tarifkanten done");
        this.tarifkanteRepoState.updateIsLoading(false);
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
        if (this.connectionState.isTrackChanges()) {
            this.updateWhenChanged();
        }

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
    private synchronized void updateWhenChanged() {
        // skip check in subsequent requests within 5 seconds
        if (LocalDateTime.now().isBefore(this.lastChangeCheck.plusSeconds(DEBOUNCE_TIME_LAST_CHANGE_CHECK_SEC))) {
            return;
        }

        logger.info("checking for changes in tks...");
        var versions = this.tkPersistenceRepo.readVersions(new ReadFilter(null, this.lastChangeCheck));
        if (versions.size() > 0) {
            logger.info(String.format("new/changed tk versions found: %s", versions.stream().map(TarifkanteVersion::getId).collect(Collectors.toList())));
            // TODO: read all versions of same element
            var elementIds = versions.stream().map(TarifkanteVersion::getElementId).distinct().collect(Collectors.toList());
            var elements = this.tkPersistenceRepo.readElements(new ReadFilter(elementIds, null));

            // update versioned object map
            elements.forEach(e -> this.versionedObjectMap.putElement(e));
            versions.forEach(v -> this.versionedObjectMap.putVersion(v));

            // update quad tree
            versions.forEach(tkV -> {
                this.versionQuadTree.removeItem(tkV.getId());
                var item = this.createQuadTreeItem(tkV);
                if (item != null) {
                    this.versionQuadTree.addItem(item);
                }
            });

        }

        this.lastChangeCheck = LocalDateTime.now();
        logger.info("done.");
    }
}
