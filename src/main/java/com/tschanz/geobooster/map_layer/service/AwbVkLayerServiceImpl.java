package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.map_layer.model.AwbVkLayerRequest;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz_repo.service.LinieVarianteRepo;
import com.tschanz.geobooster.netz_repo.service.VerwaltungRepo;
import com.tschanz.geobooster.tarif_repo.service.AwbRepo;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning.service.VersioningHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AwbVkLayerServiceImpl implements AwbVkLayerService {
    private final LinieVarianteRepo linieVarianteRepo;
    private final VerwaltungRepo verwaltungRepo;
    private final AwbRepo awbRepo;


    @Override
    public Collection<VerkehrskanteVersion> searchObjects(AwbVkLayerRequest request) {
        var awbVersion = this.awbRepo.getVersion(request.getAwbVersionId());

        // add zonenplan kanten
        var vksByZp = this.awbRepo.searchZpVerkehrskanten(awbVersion, request.getDate(), request.getBbox());
        var vkVersions = new ArrayList<>(vksByZp);

        // add verwaltung kanten
        var vksByVerw = this.awbRepo.searchVerwaltungKanten(awbVersion, request.getDate(), request.getBbox());
        vkVersions.addAll(vksByVerw);

        // TODO: minus exclude vka

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

        return vkVersions.stream()
            .filter(vkV -> VersioningHelper.isVersionInTimespan(vkV, request.getDate()))
            .filter(vkV -> vkV.hasOneOfVerwaltungAndVmTypes(request.getVmTypes(), verwaltungMap))
            .filter(vkV -> request.isShowTerminiert() || vkV.getTerminiertPer() == null || vkV.getTerminiertPer().isAfter(request.getDate()))
            .filter(vkV -> vkByLinieMap.isEmpty() || vkByLinieMap.containsKey(vkV.getId()))
            .collect(Collectors.toList());
    }
}
