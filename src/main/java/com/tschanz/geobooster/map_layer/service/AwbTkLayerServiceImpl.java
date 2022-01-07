package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.map_layer.model.AwbTkLayerRequest;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz_repo.service.TarifkanteRepo;
import com.tschanz.geobooster.rtm_repo.service.RgAuspraegungRepo;
import com.tschanz.geobooster.tarif_repo.service.AwbRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AwbTkLayerServiceImpl implements AwbTkLayerService {
    private final AwbRepo awbRepo;
    private final RgAuspraegungRepo rgAuspraegungRepo;
    private final TarifkanteRepo tkRepo;


    @Override
    public Collection<TarifkanteVersion> searchObjects(AwbTkLayerRequest request) {
        var awb = this.awbRepo.getVersion(request.getAwbVersionId());
        var tkIds = awb.getIncludeRgaIds().stream()
            .map(rgaId -> this.rgAuspraegungRepo.getElementVersionAtDate(rgaId, request.getDate()))
            .filter(Objects::nonNull)
            .flatMap(rgaV -> rgaV.getTarifkantenIds().stream())
            .collect(Collectors.toList());

        return tkIds.stream()
            .map(tkId -> this.tkRepo.getElementVersionAtDate(tkId, request.getDate()))
            .collect(Collectors.toList());
    }
}
