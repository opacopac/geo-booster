package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.map_layer.model.TarifkanteLayerRequest;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz_repo.service.LinieVarianteRepo;
import com.tschanz.geobooster.netz_repo.service.TarifkanteRepo;
import com.tschanz.geobooster.netz_repo.service.VerwaltungRepo;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning.service.VersioningHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TarifkanteLayerServiceImpl implements TarifkanteLayerService {
    private final VerwaltungRepo verwaltungRepo;
    private final TarifkanteRepo tarifkanteRepo;
    private final LinieVarianteRepo linieVarianteRepo;


    @Override
    public Collection<TarifkanteVersion> searchObjects(TarifkanteLayerRequest request) {
        Collection<TarifkanteVersion> tkVersions;
        if (!request.getLinieVarianteIds().isEmpty()) {
            tkVersions = this.linieVarianteRepo.searchTarifkanteVersions(request.getLinieVarianteIds(), request.getDate(), request.getBbox());
        } else {
            tkVersions = this.tarifkanteRepo.searchByExtent(request.getBbox());
        }

        var verwaltungIds = request.getVerwaltungVersionIds().stream()
            .map(verwVId -> this.verwaltungRepo.getVersion(verwVId).getElementId())
            .collect(Collectors.toList());
        var verwaltungMap = ArrayHelper.create1to1LookupMap(verwaltungIds, k -> k, k -> k);

        return tkVersions.stream()
            .filter(tkV -> VersioningHelper.isVersionInTimespan(tkV, request.getDate()))
            .filter(tkV -> this.tarifkanteRepo.hasOneOfVerwaltungAndVmTypes(tkV, request.getVmTypes(), verwaltungMap))
            .collect(Collectors.toList());
    }
}
