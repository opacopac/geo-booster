package com.tschanz.geobooster.tarif_repo.service;

import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz_repo.model.ProgressState;
import com.tschanz.geobooster.netz_repo.service.TarifkanteRepo;
import com.tschanz.geobooster.netz_repo.service.VerkehrskanteRepo;
import com.tschanz.geobooster.rtm_repo.service.RgAuspraegungRepo;
import com.tschanz.geobooster.tarif.model.Awb;
import com.tschanz.geobooster.tarif.model.AwbVersion;
import com.tschanz.geobooster.tarif_persistence.service.AwbPersistence;
import com.tschanz.geobooster.tarif_repo.model.AwbRepoState;
import com.tschanz.geobooster.versioning_repo.model.VersionedObjectMap;
import com.tschanz.geobooster.zone_repo.service.ZonenplanRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AwbRepoImpl implements AwbRepo {
    private final AwbPersistence awbPersistence;
    private final ProgressState progressState;
    private final AwbRepoState awbRepoState;
    private final RgAuspraegungRepo rgAuspraegungRepo;
    private final ZonenplanRepo zonenplanRepo;
    private final TarifkanteRepo tkRepo;
    private final VerkehrskanteRepo vkRepo;

    private VersionedObjectMap<Awb, AwbVersion> versionedObjectMap;


    @Override
    public void loadAll() {
        this.awbRepoState.updateIsLoading(true);

        this.progressState.updateProgressText("loading awbs...");
        var elements = this.awbPersistence.readAllElements();
        this.awbRepoState.updateLoadedElementCount(elements.size());

        this.progressState.updateProgressText("loading awb versions...");
        var versions = this.awbPersistence.readAllVersions();
        this.awbRepoState.updateLoadedVersionCount(versions.size());

        this.progressState.updateProgressText("initializing awb repo...");
        this.versionedObjectMap = new VersionedObjectMap<>(elements, versions);

        this.progressState.updateProgressText("loading awbs done");
        this.awbRepoState.updateIsLoading(false);
    }


    @Override
    public Awb getElement(long id) {
        return this.versionedObjectMap.getElement(id);
    }


    @Override
    public AwbVersion getVersion(long id) {
        return this.versionedObjectMap.getVersion(id);
    }


    @Override
    public Collection<AwbVersion> getElementVersions(long elementId) {
        return this.versionedObjectMap.getElementVersions(elementId);
    }


    @Override
    public AwbVersion getElementVersionAtDate(long elementId, LocalDate date) {
        return this.versionedObjectMap.getElementVersionAtDate(elementId, date);
    }


    @Override
    public Collection<TarifkanteVersion> getRgaTarifkanten(AwbVersion awbVersion, LocalDate date, Extent bbox) {
        var rgaIds = awbVersion.getIncludeRgaIds();
        Collection<Long> tkIds = rgaIds == null ? Collections.emptyList() : rgaIds.stream()
            .map(rgaId -> this.rgAuspraegungRepo.getElementVersionAtDate(rgaId, date))
            .filter(Objects::nonNull)
            .flatMap(rgaV -> rgaV.getTarifkantenIds().stream())
            .collect(Collectors.toList());

        var tkVs = tkIds.stream()
            .map(tkId -> this.tkRepo.getElementVersionAtDate(tkId, date))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        var bboxLonLat = new Extent(
            CoordinateConverter.convertToEpsg4326(bbox.getMinCoordinate()),
            CoordinateConverter.convertToEpsg4326(bbox.getMaxCoordinate())
        );

        var filteredRgaTkVs = tkVs.stream()
            .filter(tkV -> {
                // filter by bbox
                var tkExtent = Extent.fromAny2Coords(
                    this.tkRepo.getStartCoordinate(tkV),
                    this.tkRepo.getEndCoordinate(tkV)
                );

                return tkExtent.isExtentIntersecting(bboxLonLat);
            })
            .collect(Collectors.toList());

        return filteredRgaTkVs;
    }


    @Override
    public Collection<VerkehrskanteVersion> getZpVerkehrskanten(AwbVersion awbVersion, LocalDate date, Extent bbox) {
        var zpIds = awbVersion.getIncludeZonenplanIds();
        Collection<Long> vkIds = zpIds == null ? Collections.emptyList() : zpIds.stream()
            .map(zpId -> this.zonenplanRepo.getElementVersionAtDate(zpId, date))
            .filter(Objects::nonNull)
            .flatMap(zpV -> zpV.getVerkehrskantenIds().stream())
            .collect(Collectors.toList());

        var vkVs = vkIds.stream()
            .map(vkId -> this.vkRepo.getElementVersionAtDate(vkId, date))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        var bboxLonLat = new Extent(
            CoordinateConverter.convertToEpsg4326(bbox.getMinCoordinate()),
            CoordinateConverter.convertToEpsg4326(bbox.getMaxCoordinate())
        );

        var filteredZpVkVs = vkVs.stream()
            .filter(vkV -> {
                // filter by bbox
                var vkExtent = Extent.fromAny2Coords(
                    this.vkRepo.getStartCoordinate(vkV),
                    this.vkRepo.getEndCoordinate(vkV)
                );

                return vkExtent.isExtentIntersecting(bboxLonLat);
            })
            .collect(Collectors.toList());

        return filteredZpVkVs;
    }
}
