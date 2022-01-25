package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.map_layer.model.UnmappedTarifkanteLayerRequest;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;
import com.tschanz.geobooster.netz_repo.service.TarifkanteRepo;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning.service.VersioningHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UnmappedTarifkanteLayerServiceImpl implements UnmappedTarifkanteLayerService {
    private final TarifkanteRepo tarifkanteRepo;


    @Override
    public Collection<TarifkanteVersion> searchObjects(UnmappedTarifkanteLayerRequest request) {
        var tkVersions = this.tarifkanteRepo.searchByExtent(request.getBbox());
        var verwaltungMap = ArrayHelper.create1to1LookupMap(request.getVerwaltungVersionIds(), k -> k, k -> k);

        return tkVersions.stream()
            .filter(unmTkV -> unmTkV.getVerkehrskanteIds().size() == 0)
            .filter(unmTkV -> VersioningHelper.isVersionInTimespan(unmTkV, request.getDate()))
            .filter(unmTkV -> this.tarifkanteRepo.hasOneOfVerwaltungAndVmTypes(unmTkV, VerkehrsmittelTyp.ANY, verwaltungMap))
            .collect(Collectors.toList());
    }
}
