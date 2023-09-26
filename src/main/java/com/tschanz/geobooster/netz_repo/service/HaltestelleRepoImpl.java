package com.tschanz.geobooster.netz_repo.service;


import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz_persistence.service.HaltestellenPersistence;
import com.tschanz.geobooster.netz_repo.model.HaltestelleRepoState;
import com.tschanz.geobooster.netz_repo.model.ProgressState;
import com.tschanz.geobooster.netz_repo.model.QuadTreeConfig;
import com.tschanz.geobooster.quadtree.model.QuadTree;
import com.tschanz.geobooster.quadtree.model.QuadTreeItem;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning.model.Pflegestatus;
import com.tschanz.geobooster.versioning_repo.model.VersionedObjectMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class HaltestelleRepoImpl implements HaltestelleRepo {
    private final HaltestellenPersistence hstPersistenceRepo;
    private final ProgressState progressState;
    private final HaltestelleRepoState haltestelleRepoState;

    private Collection<Haltestelle> elements;
    private Collection<HaltestelleVersion> versions;
    private VersionedObjectMap<Haltestelle, HaltestelleVersion> versionedObjectMap;
    private QuadTree<HaltestelleVersion> versionQuadTree;
    private Map<Integer, Haltestelle> uicLookupMap;


    @Override
    public void loadData() {
        this.haltestelleRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("loading haltestellen...");
        this.elements = this.hstPersistenceRepo.readAllElements();
        this.haltestelleRepoState.updateLoadedElementCount(this.elements.size());

        this.progressState.updateProgressText("loading haltestelle versions...");
        this.versions = this.hstPersistenceRepo.readAllVersions();
        this.haltestelleRepoState.updateLoadedVersionCount(this.versions.size());

        this.progressState.updateProgressText("loading haltestellen done");
        this.haltestelleRepoState.updateIsLoading(false);
    }


    @Override
    public void initRepo() {
        this.haltestelleRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("initializing haltestelle repo...");
        this.versionedObjectMap = new VersionedObjectMap<>(this.elements, this.versions);

        this.versionQuadTree = new QuadTree<>(QuadTreeConfig.MAX_TREE_DEPTH);
        this.versionQuadTree.build(this.versions, HaltestelleVersion::getCoordinate);

        this.uicLookupMap = ArrayHelper.create1to1LookupMap(this.elements, Haltestelle::getUicCode, k -> k);

        this.elements.clear();
        this.versions.clear();

        this.progressState.updateProgressText("initializing haltestellen done");
        this.haltestelleRepoState.updateIsLoading(false);
    }


    @Override
    public List<HaltestelleVersion> searchByExtent(Extent<Epsg3857Coordinate> extent) {
        return this.versionQuadTree.findItems(extent).stream()
            .map(QuadTreeItem::getItem)
            .collect(Collectors.toList());
    }


    @Override
    public Haltestelle getByUic(int uicCode) {
        return this.uicLookupMap.get(uicCode);
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
        return this.versionedObjectMap.getElementVersionAtDate(elementId, date, Pflegestatus.PRODUKTIV);
    }
}
