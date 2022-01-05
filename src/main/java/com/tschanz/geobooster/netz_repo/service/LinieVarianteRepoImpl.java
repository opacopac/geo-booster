package com.tschanz.geobooster.netz_repo.service;

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
    private final LinieVariantePersistence linieVariantePersistence;
    private final ProgressState progressState;
    private final LinieVarianteRepoState linieVarianteRepoState;
    private final Map<Collection<Long>, Collection<Long>> linienVariantenVkIdCache = new HashMap<>();


    @Override
    public Collection<VerkehrskanteVersion> searchLinienVarianteVerkehrskantenVersions(Collection<Long> linienVarianteIds, LocalDate date) {
        var vkIds = this.linienVariantenVkIdCache.get(linienVarianteIds);
        if (vkIds == null) {
            this.linieVarianteRepoState.updateIsLoading(true);
            this.progressState.updateProgressText("loading linienvariante vks...");

            vkIds = this.linieVariantePersistence.searchVerkehrskantenIdsByLinienVarianteIds(linienVarianteIds);
            this.linienVariantenVkIdCache.put(linienVarianteIds, vkIds);

            this.progressState.updateProgressText("loading linienvariante vks done");
            this.linieVarianteRepoState.updateIsLoading(false);
        }

        return vkIds.stream()
            .map(vkId -> this.verkehrskanteRepo.getElementVersionAtDate(vkId, date))
            .collect(Collectors.toList());
    }
}
