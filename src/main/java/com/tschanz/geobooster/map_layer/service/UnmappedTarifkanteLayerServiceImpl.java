package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.map_layer.model.UnmappedTarifkanteLayerRequest;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz_repo.service.LinieVarianteRepo;
import com.tschanz.geobooster.netz_repo.service.TarifkanteRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UnmappedTarifkanteLayerServiceImpl implements UnmappedTarifkanteLayerService {
    private final TarifkanteRepo tarifkanteRepo;
    private final LinieVarianteRepo linieVarianteRepo;


    @Override
    public Collection<TarifkanteVersion> searchObjects(UnmappedTarifkanteLayerRequest request) {
        var tkVersions = this.tarifkanteRepo.searchByExtent(request.getBbox());

        return tkVersions.stream()
            .filter(tkV -> request.getDate().isEqual(tkV.getGueltigVon()) || request.getDate().isAfter(tkV.getGueltigVon()))
            .filter(tkV -> request.getDate().isEqual(tkV.getGueltigBis()) || request.getDate().isBefore(tkV.getGueltigBis()))
            .filter(tkV -> tkV.getVerkehrskanteIds().size() == 0)
            .collect(Collectors.toList());
    }
}
