package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.map_layer.model.VerkehrskanteLayerRequest;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz_repo.service.LinieVarianteRepo;
import com.tschanz.geobooster.netz_repo.service.VerkehrskanteRepo;
import com.tschanz.geobooster.netz_repo.service.VerwaltungRepo;
import com.tschanz.geobooster.versioning.service.VersioningHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class VerkehrskanteLayerServiceImpl implements VerkehrskanteLayerService {
    private final VerwaltungRepo verwaltungRepo;
    private final VerkehrskanteRepo verkehrskanteRepo;
    private final LinieVarianteRepo linieVarianteRepo;


    @Override
    public Collection<VerkehrskanteVersion> searchObjects(VerkehrskanteLayerRequest request) {
        Collection<VerkehrskanteVersion> vkVersions;
        if (request.getLinieVarianteIds().size() > 0) {
            vkVersions = this.linieVarianteRepo.searchVerkehrskanteVersions(request.getLinieVarianteIds(), request.getDate());
        } else {
            vkVersions = this.verkehrskanteRepo.searchByExtent(request.getBbox());
        }

        Map<Long, Long> verwaltungIdMap = new HashMap<>();
        request.getVerwaltungVersionIds().stream()
            .map(verwVId -> this.verwaltungRepo.getVersion(verwVId).getElementId())
            .forEach(verwEid -> verwaltungIdMap.put(verwEid, verwEid));

        return vkVersions.stream()
            .filter(vkV -> VersioningHelper.isVersionInTimespan(vkV, request.getDate()))
            .filter(vkV -> vkV.hasOneOfVerwaltungAndVmTypes(request.getVmTypes(), verwaltungIdMap))
            .filter(vkV -> request.isShowTerminiert() || vkV.getTerminiertPer() == null || vkV.getTerminiertPer().isAfter(request.getDate()))
            .collect(Collectors.toList());
    }
}
