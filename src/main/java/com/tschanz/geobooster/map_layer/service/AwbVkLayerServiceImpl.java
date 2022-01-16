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

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AwbVkLayerServiceImpl implements AwbVkLayerService {
    private final LinieVarianteRepo linieVarianteRepo;
    private final VerwaltungRepo verwaltungRepo;
    private final AwbRepo awbRepo;


    @Override
    public Collection<VerkehrskanteVersion> searchObjects(AwbVkLayerRequest request) {
        List<VerkehrskanteVersion> vkVersions = new ArrayList<>();
        var awbVersion = this.awbRepo.getVersion(request.getAwbVersionId());

        // add zonenplan kanten
        if (!awbVersion.getIncludeZonenplanIds().isEmpty()) {
            var vksByZp = this.awbRepo.searchZpVerkehrskanten(awbVersion, request.getDate(), request.getBbox());
            vkVersions.addAll(vksByZp);
        }

        // add verwaltung kanten
        if (!awbVersion.getIncludeVerwaltungIds().isEmpty()) {
            var vksByVerw = this.awbRepo.searchVerwaltungKanten(awbVersion, request.getDate(), request.getBbox());
            vkVersions.addAll(vksByVerw);
        }

        // TODO: minus exclude vka

        // linien filter
        var vksByLinie = !request.getLinieVarianteIds().isEmpty()
            ? this.linieVarianteRepo.searchVerkehrskanteVersions(request.getLinieVarianteIds(), request.getDate())
            : Collections.<VerkehrskanteVersion>emptyList();
        var vkByLinieMap = ArrayHelper.create1to1LookupMap(vksByLinie, VerkehrskanteVersion::getId, k -> k);

        // verwaltung filter
        Map<Long, Long> filterVerwaltungIdMap = new HashMap<>();
        request.getVerwaltungVersionIds().stream()
            .map(verwVId -> this.verwaltungRepo.getVersion(verwVId).getElementId())
            .forEach(verwEid -> filterVerwaltungIdMap.put(verwEid, verwEid));

        return vkVersions.stream()
            .filter(vkV -> VersioningHelper.isVersionInTimespan(vkV, request.getDate()))
            .filter(vkV -> request.getVmTypes().isEmpty() || vkV.hasOneOfVmTypes(request.getVmTypes()))
            .filter(vkV -> filterVerwaltungIdMap.isEmpty() || vkV.hasOneOfVerwaltungIds(filterVerwaltungIdMap))
            .filter(vkV -> request.isShowTerminiert() || vkV.getTerminiertPer() == null || vkV.getTerminiertPer().isAfter(request.getDate()))
            .filter(vkV -> vkByLinieMap.isEmpty() || vkByLinieMap.containsKey(vkV.getId()))
            .collect(Collectors.toList());
    }
}
