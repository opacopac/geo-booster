package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.map_layer.model.MapLayerRequest;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz_repo.service.HaltestelleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class HaltestelleLayerServiceImpl implements HaltestelleLayerService {
    private final HaltestelleRepo haltestelleRepo;


    @Override
    public Collection<HaltestelleVersion> searchObjects(MapLayerRequest request) {
        var hstVersions = this.haltestelleRepo.searchByExtent(request.getBbox());

        return hstVersions.stream()
            .filter(hstv -> request.getDate().isEqual(hstv.getGueltigVon()) || request.getDate().isAfter(hstv.getGueltigVon()))
            .filter(hstv -> request.getDate().isEqual(hstv.getGueltigBis()) || request.getDate().isBefore(hstv.getGueltigBis()))
            .collect(Collectors.toList());
    }
}
