package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.map_layer.model.AwbTkLayerRequest;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz_repo.service.TarifkanteRepo;
import com.tschanz.geobooster.tarif_repo.service.AwbRepo;
import com.tschanz.geobooster.versioning.service.VersioningHelper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AwbTkLayerServiceImpl implements AwbTkLayerService {
    private static final Logger logger = LogManager.getLogger(AwbTkLayerServiceImpl.class);


    private final AwbRepo awbRepo;
    private final TarifkanteRepo tarifkanteRepo;


    @Override
    public Collection<TarifkanteVersion> searchObjects(AwbTkLayerRequest request) {
        var awbVersion = this.awbRepo.getVersion(request.getAwbVersionId());
        var rgaTkVs = this.awbRepo.getRgaTarifkanten(awbVersion, request.getDate(), request.getBbox());

        return rgaTkVs.stream()
            .filter(tkV -> VersioningHelper.isVersionInTimespan(tkV, request.getDate()))
            .collect(Collectors.toList());
    }
}
