package com.tschanz.geobooster.map_layer.service;

import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.map_layer.model.AwbTkLayerRequest;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz_repo.service.TarifkanteRepo;
import com.tschanz.geobooster.tarif_repo.service.AwbRepo;
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
        logger.info("start");
        var awbVersion = this.awbRepo.getVersion(request.getAwbVersionId());
        logger.info("awbversions");
        var rgaTkVs = this.awbRepo.getRgaTarifkanten(awbVersion, request.getDate());
        logger.info("rgaTkvs");
        var bboxLonLat = new Extent(
            CoordinateConverter.convertToEpsg4326(request.getBbox().getMinCoordinate()),
            CoordinateConverter.convertToEpsg4326(request.getBbox().getMaxCoordinate())
        );
        var filteredRgaTkVs = rgaTkVs.stream()
            .filter(tkV -> {
                // filter by bbox
                var tkExtent = Extent.fromAny2Coords(
                    this.tarifkanteRepo.getStartCoordinate(tkV),
                    this.tarifkanteRepo.getEndCoordinate(tkV)
                );

                return tkExtent.isExtentIntersecting(bboxLonLat);
            })
            .collect(Collectors.toList());
        logger.info("filteredRgaTkvs");

        return filteredRgaTkVs;
    }
}
