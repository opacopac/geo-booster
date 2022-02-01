package com.tschanz.geobooster.utfgrid_composer.model;

import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.map_style.model.HaltestelleStyle;
import com.tschanz.geobooster.map_style.model.MapStyle;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz_repo.service.HaltestelleRepo;
import com.tschanz.geobooster.utfgrid.model.UtfGridPointItem;
import com.tschanz.geobooster.util.model.Tuple2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;


@Component
@RequiredArgsConstructor
public class UtfGridHaltestelleConverter {
    private final HaltestelleRepo hstRepo;


    public UtfGridPointItem toUtfGrid(HaltestelleVersion hstVersion, float zoomLevel, Collection<MapStyle> mapStyles) {
        var hstE = this.hstRepo.getElement(hstVersion.getElementId());
        var latLon = CoordinateConverter.convertToEpsg4326(hstVersion.getCoordinate());

        return new UtfGridPointItem(
            hstVersion.getCoordinate(),
            HaltestelleStyle.WIDTH.getWidth(zoomLevel),
            Arrays.asList(
                new Tuple2<>("id", "HALTESTELLEN." + hstVersion.getId()),
                new Tuple2<>("ID", hstVersion.getId()),
                new Tuple2<>("UIC_CODE", hstE.getUicCode()),
                new Tuple2<>("NAME", hstVersion.getName()),
                new Tuple2<>("LAT", (float) latLon.getLatitude()),
                new Tuple2<>("LNG", (float) latLon.getLongitude())
            )
        );
    }
}
