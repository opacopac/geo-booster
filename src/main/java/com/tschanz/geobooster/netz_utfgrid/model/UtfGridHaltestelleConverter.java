package com.tschanz.geobooster.netz_utfgrid.model;

import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz_maptile.model.HaltestelleStyle;
import com.tschanz.geobooster.netz_repo.service.HaltestelleRepo;
import com.tschanz.geobooster.utfgrid.model.UtfGridPointItem;
import com.tschanz.geobooster.util.model.KeyValue;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;


@RequiredArgsConstructor
public class UtfGridHaltestelleConverter {
    private final HaltestelleRepo hstRepo;


    public UtfGridPointItem toUtfGrid(HaltestelleVersion hstVersion, float zoomLevel) {
        var hstE = this.hstRepo.getElement(hstVersion.getElementId());

        return new UtfGridPointItem(
            CoordinateConverter.convertToEpsg3857(hstVersion.getCoordinate()),
            HaltestelleStyle.WIDTH.getWidth(zoomLevel),
            Arrays.asList(
                new KeyValue<>("id", "HALTESTELLEN." + hstVersion.getId()),
                new KeyValue<>("ID", hstVersion.getId()),
                new KeyValue<>("UIC_CODE", hstE.getUicCode()),
                new KeyValue<>("NAME", hstVersion.getName()),
                new KeyValue<>("LAT", hstVersion.getCoordinate().getLatitude()),
                new KeyValue<>("LNG", hstVersion.getCoordinate().getLongitude())
            )
        );
    }
}
