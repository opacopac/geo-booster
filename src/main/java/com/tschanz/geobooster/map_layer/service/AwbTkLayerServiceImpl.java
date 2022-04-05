package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.map_layer.model.AwbTkLayerRequest;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz_repo.service.LinieVarianteRepo;
import com.tschanz.geobooster.netz_repo.service.TarifkanteRepo;
import com.tschanz.geobooster.netz_repo.service.VerwaltungRepo;
import com.tschanz.geobooster.tarif_repo.service.AwbRepo;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning.service.VersioningHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AwbTkLayerServiceImpl implements AwbTkLayerService {
    private final TarifkanteRepo tkRepo;
    private final AwbRepo awbRepo;
    private final LinieVarianteRepo linieVarianteRepo;
    private final VerwaltungRepo verwaltungRepo;


    @Override
    public Collection<TarifkanteVersion> searchObjects(AwbTkLayerRequest request) {
        var awbVersion = this.awbRepo.getVersion(request.getAwbVersionId());
        var rgaTkVs = this.awbRepo.searchRgaTarifkanten(awbVersion, request.getDate(), request.getBbox());

        // linien filter
        var vksByLinie = !request.getLinieVarianteIds().isEmpty()
                ? this.linieVarianteRepo.searchVerkehrskanteVersions(request.getLinieVarianteIds(), request.getDate(), request.getBbox())
                : Collections.<VerkehrskanteVersion>emptyList();
        var vkByLinieMap = ArrayHelper.create1to1LookupMap(vksByLinie, VerkehrskanteVersion::getId, k -> k);

        // verwaltung filter
        var verwaltungIds = request.getVerwaltungVersionIds().stream()
                .map(verwVId -> this.verwaltungRepo.getVersion(verwVId).getElementId())
                .collect(Collectors.toList());
        var verwaltungMap = ArrayHelper.create1to1LookupMap(verwaltungIds, k -> k, k -> k);

        return rgaTkVs.stream()
            .filter(tkV -> VersioningHelper.isVersionInTimespan(tkV, request.getDate()))
            .filter(tkV -> this.tkRepo.hasOneOfVerwaltungAndVmTypes(tkV, request.getVmTypes(), verwaltungMap))
            .filter(tkV -> vkByLinieMap.isEmpty() || vkByLinieMap.containsKey(tkV.getId()))
            .collect(Collectors.toList());
    }
}
