package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.map_layer.model.HaltestelleWegangabeLayerRequest;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabeVersion;
import com.tschanz.geobooster.rtm_repo.service.HaltestelleWegangabeRepo;
import com.tschanz.geobooster.versioning.service.VersioningHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
@RequiredArgsConstructor
public class HaltestelleWegangabeLayerServiceImpl implements HaltestelleWegangabeLayerService {
    private final HaltestelleWegangabeRepo hstWegangabeRepo;


    @Override
    public Collection<HaltestelleWegangabeVersion> searchObjects(HaltestelleWegangabeLayerRequest request) {
        var hstVersions = this.hstWegangabeRepo.searchByExtent(request.getBbox());

        return VersioningHelper.filterVersions(hstVersions, request.getDate());
    }
}
