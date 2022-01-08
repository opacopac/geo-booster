package com.tschanz.geobooster.rtm_repo.service;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz_repo.model.ProgressState;
import com.tschanz.geobooster.netz_repo.model.QuadTreeConfig;
import com.tschanz.geobooster.netz_repo.service.HaltestelleRepo;
import com.tschanz.geobooster.quadtree.model.QuadTree;
import com.tschanz.geobooster.quadtree.model.QuadTreeItem;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabe;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabeVersion;
import com.tschanz.geobooster.rtm_persistence.service.HaltestelleWegangabePersistence;
import com.tschanz.geobooster.rtm_repo.model.HaltestelleWegangabeRepoState;
import com.tschanz.geobooster.versioning_repo.model.VersionedObjectMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class HaltestelleWegangabeRepoImpl implements HaltestelleWegangabeRepo {
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

        this.versionQuadTree = new QuadTree<>(QuadTreeConfig.MAX_TREE_DEPTH);
        this.versionQuadTree.build(versions, this::getHaltestelleCoordinate);

        this.progressState.updateProgressText("loading haltestelle wegangaben done");
        this.hstWegangabeRepoState.updateIsLoading(false);
    }


    @Override
    public List<HaltestelleWegangabeVersion> searchByExtent(Extent<Epsg3857Coordinate> extent) {
        return this.versionQuadTree.findItems(extent).stream()
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
    public Epsg3857Coordinate getHaltestelleCoordinate(HaltestelleWegangabeVersion hstWegangabeVersion) {
        var hstV = this.getHaltestelleVersion(hstWegangabeVersion);

        return hstV != null
            ? hstV.getCoordinate()
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
}
