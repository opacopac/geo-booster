package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.map_layer.model.TarifkanteLayerRequest;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;
import com.tschanz.geobooster.netz_repo.service.LinieVarianteRepo;
import com.tschanz.geobooster.netz_repo.service.TarifkanteRepo;
import com.tschanz.geobooster.netz_repo.service.VerkehrskanteRepo;
import com.tschanz.geobooster.netz_repo.service.VerwaltungRepo;
import com.tschanz.geobooster.versioning.service.VersioningHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TarifkanteLayerServiceImpl implements TarifkanteLayerService {
    private final VerwaltungRepo verwaltungRepo;
    private final VerkehrskanteRepo verkehrskanteRepo;
    private final TarifkanteRepo tarifkanteRepo;
    private final LinieVarianteRepo linieVarianteRepo;


    @Override
    public Collection<TarifkanteVersion> searchObjects(TarifkanteLayerRequest request) {
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
            .filter(tkV -> VersioningHelper.isVersionInTimespan(tkV, request.getDate()))
            .filter(tkV -> request.getVmTypes().isEmpty() || this.hasOneOfVmTypes(tkV, request.getVmTypes()))
            .filter(tkV -> verwaltungIds.isEmpty() || this.hasOneOfVerwaltungIds(tkV, verwaltungIds))
            .collect(Collectors.toList());
    }


    private boolean hasOneOfVmTypes(TarifkanteVersion tkVersion, Collection<VerkehrsmittelTyp> vmTypes) {
        var tkVmTypes = this.getVerkehrskanteVersions(tkVersion)
            .stream()
            .flatMap(vkv -> vkv.getVmTypes().stream())
            .collect(Collectors.toList());

        var vmTypeBitmask = VerkehrsmittelTyp.getBitMask(tkVmTypes);
        return (VerkehrsmittelTyp.getBitMask(vmTypes) & vmTypeBitmask) > 0;
    }


    private boolean hasOneOfVerwaltungIds(TarifkanteVersion tkVersion, Collection<Long> verwaltungIds) {
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
