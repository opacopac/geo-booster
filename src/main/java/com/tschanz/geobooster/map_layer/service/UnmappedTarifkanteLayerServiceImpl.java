package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.map_layer.model.UnmappedTarifkanteLayerRequest;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz_repo.service.TarifkanteRepo;
import com.tschanz.geobooster.versioning.service.VersioningHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
@RequiredArgsConstructor
public class UnmappedTarifkanteLayerServiceImpl implements UnmappedTarifkanteLayerService {
    private final TarifkanteRepo tarifkanteRepo;


    @Override
    public Collection<TarifkanteVersion> searchObjects(UnmappedTarifkanteLayerRequest request) {
        var tkVersions = this.tarifkanteRepo.searchByExtent(request.getBbox());

        return VersioningHelper.filterVersions(tkVersions, request.getDate());
    }
}
