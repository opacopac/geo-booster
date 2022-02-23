package com.tschanz.geobooster.netz_repo.service;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz_persistence.service.LinieVariantePersistence;
import com.tschanz.geobooster.netz_repo.model.LinieVarianteRepoState;
import com.tschanz.geobooster.netz_repo.model.ProgressState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class LinieVarianteRepoImpl implements LinieVarianteRepo {
    private final VerkehrskanteRepo verkehrskanteRepo;
    private final TarifkanteRepo tarifkanteRepo;
    private final LinieVariantePersistence linieVariantePersistence;
    private final ProgressState progressState;
    private final LinieVarianteRepoState linieVarianteRepoState;
    private final Map<Collection<Long>, Collection<Long>> linienVariantenVkIdCache = new HashMap<>();
    private final Map<Collection<Long>, Collection<Long>> linienVariantenTkIdCache = new HashMap<>();


    @Override
    public Collection<VerkehrskanteVersion> searchVerkehrskanteVersions(
        Collection<Long> linienVarianteIds,
        LocalDate date,
        Extent<Epsg3857Coordinate> extent
    ) {
        var vkIds = this.linienVariantenVkIdCache.get(linienVarianteIds);
        if (vkIds == null) {
            this.linieVarianteRepoState.updateIsLoading(true);
            this.progressState.updateProgressText("loading linienvariante vks...");

            vkIds = this.linieVariantePersistence.searchVerkehrskanteIds(linienVarianteIds);
            this.linienVariantenVkIdCache.put(linienVarianteIds, vkIds);

            this.progressState.updateProgressText("loading linienvariante vks done");
            this.linieVarianteRepoState.updateIsLoading(false);
        }

        return vkIds.stream()
            .map(vkId -> this.verkehrskanteRepo.getElementVersionAtDate(vkId, date))
            .filter(vkV -> {
                return Extent.fromCoords(
                    this.verkehrskanteRepo.getStartCoordinate(vkV),
                    this.verkehrskanteRepo.getEndCoordinate(vkV)
                ).isExtentIntersecting(extent);
            })
            .collect(Collectors.toList());
    }


    @Override
    public Collection<TarifkanteVersion> searchTarifkanteVersions(
        Collection<Long> linienVarianteIds,
        LocalDate date,
        Extent<Epsg3857Coordinate> extent
    ) {
        var tkIds = this.linienVariantenTkIdCache.get(linienVarianteIds);
        if (tkIds == null) {
            this.linieVarianteRepoState.updateIsLoading(true);
            this.progressState.updateProgressText("loading linienvariante tks...");

            tkIds = this.linieVariantePersistence.searchTarifkanteIds(linienVarianteIds, date);
            this.linienVariantenTkIdCache.put(linienVarianteIds, tkIds);

            this.progressState.updateProgressText("loading linienvariante tks done");
            this.linieVarianteRepoState.updateIsLoading(false);
        }

        return tkIds.stream()
            .map(tkId -> this.tarifkanteRepo.getElementVersionAtDate(tkId, date))
            .filter(tkV -> {
                return Extent.fromCoords(
                    this.tarifkanteRepo.getStartCoordinate(tkV),
                    this.tarifkanteRepo.getEndCoordinate(tkV)
                ).isExtentIntersecting(extent);
            })
            .collect(Collectors.toList());
    }
}
