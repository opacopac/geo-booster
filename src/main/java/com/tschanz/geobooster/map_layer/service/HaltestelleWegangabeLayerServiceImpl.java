package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.map_layer.model.HaltestelleWegangabeLayerRequest;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabeVersion;
import com.tschanz.geobooster.rtm_repo.service.HaltestelleWegangabeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class HaltestelleWegangabeLayerServiceImpl implements HaltestelleWegangabeLayerService {
    private final HaltestelleWegangabeRepo hstWegangabeRepo;


    @Override
    public Collection<HaltestelleWegangabeVersion> searchObjects(HaltestelleWegangabeLayerRequest request) {
        var hstVersions = this.hstWegangabeRepo.searchByExtent(request.getBbox());

        return hstVersions.stream()
            .filter(hstv -> request.getDate().isEqual(hstv.getGueltigVon()) || request.getDate().isAfter(hstv.getGueltigVon()))
            .filter(hstv -> request.getDate().isEqual(hstv.getGueltigBis()) || request.getDate().isBefore(hstv.getGueltigBis()))
            .collect(Collectors.toList());
    }
}
