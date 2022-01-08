package com.tschanz.geobooster.utfgrid_composer.model;

import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.map_style.model.HaltestelleStyle;
import com.tschanz.geobooster.map_style.model.MapStyle;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz_repo.service.HaltestelleRepo;
import com.tschanz.geobooster.utfgrid.model.UtfGridPointItem;
import com.tschanz.geobooster.util.model.KeyValue;
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
                new KeyValue<>("id", "HALTESTELLEN." + hstVersion.getId()),
                new KeyValue<>("ID", hstVersion.getId()),
                new KeyValue<>("UIC_CODE", hstE.getUicCode()),
                new KeyValue<>("NAME", hstVersion.getName()),
                new KeyValue<>("LAT", latLon.getLatitude()),
                new KeyValue<>("LNG", latLon.getLongitude())
            )
        );
    }
}
