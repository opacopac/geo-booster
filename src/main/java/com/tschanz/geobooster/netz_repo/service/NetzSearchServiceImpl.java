package com.tschanz.geobooster.netz_repo.service;

import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz_repo.model.SearchNetzObjectsRequest;
import com.tschanz.geobooster.netz_repo.model.SearchNetzObjectsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class NetzSearchServiceImpl implements NetzSearchService {
    private final HaltestelleRepo haltestelleRepo;
    private final VerkehrskanteRepo verkehrskanteRepo;
    private final TarifkanteRepo tarifkanteRepo;
    private final LinieVarianteRepo linieVarianteRepo;


    @Override
    public SearchNetzObjectsResponse searchNetzObjects(SearchNetzObjectsRequest request) {
        List<HaltestelleVersion> hstVersions = request.isShowHaltestellen()
            ? this.haltestelleRepo.searchVersions(request.getDate(), request.getBbox())
            : Collections.emptyList();

        Collection<VerkehrskanteVersion> vkVersions;
        Collection<TarifkanteVersion> tkVersions;
        if (request.getLinieVarianteIds().size() > 0 && (request.isShowVerkehrskanten() || request.isShowTarifkanten() || request.isShowUnmappedTarifkanten())) {
            vkVersions = request.isShowVerkehrskanten()
                ? this.linieVarianteRepo.searchVerkehrskanteVersions(request.getLinieVarianteIds(), request.getVmTypes(), request.getDate())
                : Collections.emptyList();
            tkVersions = request.isShowTarifkanten() || request.isShowUnmappedTarifkanten()
                ? this.linieVarianteRepo.searchTarifkanteVersions(request.getLinieVarianteIds(), request.getVmTypes(), request.getDate())
                : Collections.emptyList();
        } else {
            vkVersions = request.isShowVerkehrskanten()
                ? this.verkehrskanteRepo.searchVersionsByExtent(request.getDate(), request.getBbox(), request.getVmTypes(), request.getVerwaltungVersionIds(), request.isShowTerminiert())
                : Collections.emptyList();
            tkVersions = request.isShowTarifkanten() || request.isShowUnmappedTarifkanten()
                ? this.tarifkanteRepo.searchVersionsByExtent(request.getDate(), request.getBbox(), request.getVmTypes(), request.getVerwaltungVersionIds(), request.isShowUnmappedTarifkanten())
                : Collections.emptyList();
        }

        return new SearchNetzObjectsResponse(hstVersions, vkVersions, tkVersions);
    }
}
