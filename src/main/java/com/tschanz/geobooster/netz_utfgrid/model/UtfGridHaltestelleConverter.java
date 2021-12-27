package com.tschanz.geobooster.netz_utfgrid.model;

import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz_graphics.model.HaltestelleStyle;
import com.tschanz.geobooster.utfgrid.model.UtfGridPointItem;
import com.tschanz.geobooster.util.model.KeyValue;

import java.util.Arrays;


public class UtfGridHaltestelleConverter {
    public static UtfGridPointItem toUtfGrid(HaltestelleVersion haltestelleVersion, float zoomLevel) {
        return new UtfGridPointItem(
            CoordinateConverter.convertToEpsg3857(haltestelleVersion.getCoordinate()),
            HaltestelleStyle.WIDTH.getWidth(zoomLevel),
            Arrays.asList(
                new KeyValue<>("id", "HALTESTELLEN." + haltestelleVersion.getVersionInfo().getId()),
                new KeyValue<>("ID", haltestelleVersion.getVersionInfo().getId()),
                new KeyValue<>("UIC_CODE", haltestelleVersion.getVersionInfo().getElement().getUicCode()),
                new KeyValue<>("NAME", haltestelleVersion.getName()),
                new KeyValue<>("LAT", haltestelleVersion.getCoordinate().getLatitude()),
                new KeyValue<>("LNG", haltestelleVersion.getCoordinate().getLongitude())
            )
        );
    }
}
