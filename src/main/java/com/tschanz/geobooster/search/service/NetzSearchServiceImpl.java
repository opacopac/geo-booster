package com.tschanz.geobooster.search.service;

import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;
import com.tschanz.geobooster.netz_repo.service.*;
import com.tschanz.geobooster.search.model.SearchNetzObjectsRequest;
import com.tschanz.geobooster.search.model.SearchNetzObjectsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class NetzSearchServiceImpl implements NetzSearchService {
    private final VerwaltungRepo verwaltungRepo;
    private final HaltestelleRepo haltestelleRepo;
    private final VerkehrskanteRepo verkehrskanteRepo;
    private final TarifkanteRepo tarifkanteRepo;
    private final LinieVarianteRepo linieVarianteRepo;


    @Override
    public SearchNetzObjectsResponse searchNetzObjects(SearchNetzObjectsRequest request) {
        var hstVersions = this.searchHaltestellen(request);
        var vkVersions = this.searchVerkehrskanten(request);
        var tkVersions = this.searchTarifkanten(request);

        return new SearchNetzObjectsResponse(hstVersions, vkVersions, tkVersions);
    }


    private Collection<HaltestelleVersion> searchHaltestellen(SearchNetzObjectsRequest request) {
        if (!request.isShowHaltestellen()) {
            return Collections.emptyList();
        }

        var hstVersions = this.haltestelleRepo.searchByExtent(request.getBbox());

        return hstVersions.stream()
            .filter(hstv -> request.getDate().isEqual(hstv.getGueltigVon()) || request.getDate().isAfter(hstv.getGueltigVon()))
            .filter(hstv -> request.getDate().isEqual(hstv.getGueltigBis()) || request.getDate().isBefore(hstv.getGueltigBis()))
            .collect(Collectors.toList());
    }


    private Collection<VerkehrskanteVersion> searchVerkehrskanten(SearchNetzObjectsRequest request) {
        if (!request.isShowVerkehrskanten()) {
            return Collections.emptyList();
        }

        Collection<VerkehrskanteVersion> vkVersions;
        if (request.getLinieVarianteIds().size() > 0) {
            vkVersions = this.linieVarianteRepo.searchVerkehrskanteVersions(request.getLinieVarianteIds(), request.getDate());
        } else {
            vkVersions = this.verkehrskanteRepo.searchByExtent(request.getBbox());
        }

        var verwaltungIds = request.getVerwaltungVersionIds().stream()
            .map(verwVId -> this.verwaltungRepo.getVersion(verwVId).getElementId())
            .collect(Collectors.toList());

        return vkVersions.stream()
            .filter(vkV -> request.getDate().isAfter(vkV.getGueltigVon()) || request.getDate().isEqual(vkV.getGueltigVon()))
            .filter(vkV -> request.getDate().isBefore(vkV.getGueltigBis()) || request.getDate().isEqual(vkV.getGueltigBis()))
            .filter(vkV -> request.getVmTypes().isEmpty() || vkV.hasOneOfVmTypes(request.getVmTypes()))
            .filter(vkV -> verwaltungIds.isEmpty() || vkV.hasOneOfVerwaltungIds(verwaltungIds))
            .filter(vkV -> request.isShowTerminiert() || vkV.getTerminiertPer() == null || vkV.getTerminiertPer().isAfter(request.getDate()))
            .collect(Collectors.toList());
    }


    private Collection<TarifkanteVersion> searchTarifkanten(SearchNetzObjectsRequest request) {
        if (!request.isShowTarifkanten() && !request.isShowUnmappedTarifkanten()) {
            return Collections.emptyList();
        }

        Collection<TarifkanteVersion> tkVersions;
        if (request.getLinieVarianteIds().size() > 0) {
            tkVersions = this.linieVarianteRepo.searchTarifkanteVersions(request.getLinieVarianteIds(), request.getDate());
        } else {
            tkVersions = this.tarifkanteRepo.searchByExtent(request.getBbox());
        }

        var verwaltungIds = request.getVerwaltungVersionIds().stream()
            .map(verwVId -> this.verwaltungRepo.getVersion(verwVId).getElementId())
            .collect(Collectors.toList());

        return tkVersions.stream()
            .filter(tkV -> request.getDate().isEqual(tkV.getGueltigVon()) || request.getDate().isAfter(tkV.getGueltigVon()))
            .filter(tkV -> request.getDate().isEqual(tkV.getGueltigBis()) || request.getDate().isBefore(tkV.getGueltigBis()))
            .filter(tkV -> request.getVmTypes().isEmpty() || this.hasOneOfVmTypes(tkV, request.getVmTypes()))
            .filter(tkV -> {
                if (request.isShowUnmappedTarifkanten()) {
                    return tkV.getVerkehrskanteIds().size() == 0;
                } else {
                    return verwaltungIds.isEmpty() || this.hasOneOfVerwaltungIds(tkV, verwaltungIds);
                }
            })
            .collect(Collectors.toList());
    }


    private boolean hasOneOfVmTypes(TarifkanteVersion tkVersion, List<VerkehrsmittelTyp> vmTypes) {
        var tkVmTypes = this.getVerkehrskanteVersions(tkVersion)
            .stream()
            .flatMap(vkv -> vkv.getVmTypes().stream())
            .collect(Collectors.toList());

        var vmTypeBitmask = VerkehrsmittelTyp.getBitMask(tkVmTypes);
        return (VerkehrsmittelTyp.getBitMask(vmTypes) & vmTypeBitmask) > 0;
    }


    private boolean hasOneOfVerwaltungIds(TarifkanteVersion tkVersion, List<Long> verwaltungIds) {
        var tkVerwaltungen = this.getVerkehrskanteVersions(tkVersion)
            .stream()
            .flatMap(vkv -> vkv.getVerwaltungIds().stream())
            .collect(Collectors.toList());

        for (var verwaltungId: verwaltungIds) {
            if (tkVerwaltungen.contains(verwaltungId)) {
                return true;
            }
        };

        return false;
    }


    private List<VerkehrskanteVersion> getVerkehrskanteVersions(TarifkanteVersion tkVersion) {
        return tkVersion.getVerkehrskanteIds()
            .stream()
            .map(vkId -> this.verkehrskanteRepo.getElementVersionAtDate(vkId, tkVersion.getGueltigBis()))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}
