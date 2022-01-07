package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.map_layer.model.HaltestelleLayerRequest;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz_repo.service.HaltestelleRepo;
import com.tschanz.geobooster.versioning.service.VersioningHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
@RequiredArgsConstructor
public class HaltestelleLayerServiceImpl implements HaltestelleLayerService {
    private final HaltestelleRepo haltestelleRepo;


    @Override
    public Collection<HaltestelleVersion> searchObjects(HaltestelleLayerRequest request) {
        var hstVersions = this.haltestelleRepo.searchByExtent(request.getBbox());

        return VersioningHelper.filterVersions(hstVersions, request.getDate());
    }
}
