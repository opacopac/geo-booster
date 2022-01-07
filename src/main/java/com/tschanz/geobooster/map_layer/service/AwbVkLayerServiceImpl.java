package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.map_layer.model.AwbVkLayerRequest;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz_repo.service.LinieVarianteRepo;
import com.tschanz.geobooster.netz_repo.service.VerkehrskanteRepo;
import com.tschanz.geobooster.netz_repo.service.VerwaltungRepo;
import com.tschanz.geobooster.tarif_repo.service.AwbRepo;
import com.tschanz.geobooster.versioning.service.VersioningHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AwbVkLayerServiceImpl implements AwbVkLayerService {
    private final VerkehrskanteRepo verkehrskanteRepo;
    private final LinieVarianteRepo linieVarianteRepo;
    private final VerwaltungRepo verwaltungRepo;
    private final AwbRepo awbRepo;


    @Override
    public Collection<VerkehrskanteVersion> searchObjects(AwbVkLayerRequest request) {
        var vkVersions = new ArrayList<VerkehrskanteVersion>();
        var awbVersion = this.awbRepo.getVersion(request.getAwbVersionId());

        if (request.getLinieVarianteIds().size() > 0) {
            var vksByLinie = this.linieVarianteRepo.searchVerkehrskanteVersions(request.getLinieVarianteIds(), request.getDate());
            vkVersions.addAll(vksByLinie);
        } else {
            var vksByExtent = this.verkehrskanteRepo.searchByExtent(request.getBbox());
            vkVersions.addAll(vksByExtent);
        }

        Map<Long, Long> awbVerwaltungIdMap = new HashMap<>();
        awbVersion.getIncludeVerwaltungIds().forEach(verwEid -> awbVerwaltungIdMap.put(verwEid, verwEid));

        Map<Long, Long> filterVerwaltungIdMap = new HashMap<>();
        request.getVerwaltungVersionIds().stream()
            .map(verwVId -> this.verwaltungRepo.getVersion(verwVId).getElementId())
            .forEach(verwEid -> filterVerwaltungIdMap.put(verwEid, verwEid));

        return vkVersions.stream()
            .filter(vkV -> VersioningHelper.isVersionInTimespan(vkV, request.getDate()))
            .filter(vkV -> request.getVmTypes().isEmpty() || vkV.hasOneOfVmTypes(request.getVmTypes()))
            .filter(vkV -> {
                // TODO: add kanten from zone & uzone
                return vkV.hasOneOfVerwaltungIds(awbVerwaltungIdMap);
            })
            .filter(vkV -> filterVerwaltungIdMap.isEmpty() || vkV.hasOneOfVerwaltungIds(filterVerwaltungIdMap))
            .filter(vkV -> request.isShowTerminiert() || vkV.getTerminiertPer() == null || vkV.getTerminiertPer().isAfter(request.getDate()))
            .collect(Collectors.toList());
    }
}
