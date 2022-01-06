package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.map_layer.model.MapLayerRequest;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz_repo.service.LinieVarianteRepo;
import com.tschanz.geobooster.netz_repo.service.VerkehrskanteRepo;
import com.tschanz.geobooster.netz_repo.service.VerwaltungRepo;
import com.tschanz.geobooster.tarif_repo.service.AwbRepo;
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
    private final AwbRepo awbRepo;


    @Override
    public Collection<VerkehrskanteVersion> searchObjects(MapLayerRequest request) {
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
            .filter(vkV -> request.getDate().isAfter(vkV.getGueltigVon()) || request.getDate().isEqual(vkV.getGueltigVon()))
            .filter(vkV -> request.getDate().isBefore(vkV.getGueltigBis()) || request.getDate().isEqual(vkV.getGueltigBis()))
            .filter(vkV -> request.getVmTypes().isEmpty() || vkV.hasOneOfVmTypes(request.getVmTypes()))
            .filter(vkV -> verwaltungIdMap.isEmpty() || vkV.hasOneOfVerwaltungIds(verwaltungIdMap))
            .filter(vkV -> request.isShowTerminiert() || vkV.getTerminiertPer() == null || vkV.getTerminiertPer().isAfter(request.getDate()))
            .collect(Collectors.toList());
    }
}
