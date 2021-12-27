package com.tschanz.geobooster.netz_utfgrid.model;

import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz_graphics.model.VerkehrskanteStyle;
import com.tschanz.geobooster.utfgrid.model.UtfGridLineItem;
import com.tschanz.geobooster.util.model.KeyValue;

import java.util.Arrays;


public class UtfGridVerkehrskanteConverter {
    public static UtfGridLineItem toUtfGrid(VerkehrskanteVersion verkehrskanteVersion, float zoomLevel) {
        return new UtfGridLineItem(
            CoordinateConverter.convertToEpsg3857(verkehrskanteVersion.getStartCoordinate()),
            CoordinateConverter.convertToEpsg3857(verkehrskanteVersion.getEndCoordinate()),
            VerkehrskanteStyle.WIDTH.getWidth(zoomLevel),
            Arrays.asList(
                new KeyValue<>("id", "VERKEHRSKANTEN." + verkehrskanteVersion.getVersionInfo().getId()),
                new KeyValue<>("ID", verkehrskanteVersion.getVersionInfo().getId()),
                new KeyValue<>("HS1_ID", verkehrskanteVersion.getHaltestelle1().getElementInfo().getId()),
                new KeyValue<>("HS1_UIC_CODE", verkehrskanteVersion.getHaltestelle1().getUicCode()),
                new KeyValue<>("HS1_NAME", verkehrskanteVersion.getHaltestelle1Version().getName()),
                new KeyValue<>("HS1_LAT", verkehrskanteVersion.getStartCoordinate().getLatitude()),
                new KeyValue<>("HS1_LNG", verkehrskanteVersion.getStartCoordinate().getLongitude()),
                new KeyValue<>("HS2_ID", verkehrskanteVersion.getHaltestelle2().getElementInfo().getId()),
                new KeyValue<>("HS2_UIC_CODE", verkehrskanteVersion.getHaltestelle2().getUicCode()),
                new KeyValue<>("HS2_NAME", verkehrskanteVersion.getHaltestelle2Version().getName()),
                new KeyValue<>("HS2_LAT", verkehrskanteVersion.getEndCoordinate().getLatitude()),
                new KeyValue<>("HS2_LNG", verkehrskanteVersion.getEndCoordinate().getLongitude()),
                new KeyValue<>("GUELTIG_VON", verkehrskanteVersion.getVersionInfo().getGueltigVon()),
                new KeyValue<>("GUELTIG_BIS", verkehrskanteVersion.getVersionInfo().getGueltigBis()),
                new KeyValue<>("TERMINIERT_PER", null) // TODO
            )
        );
    }
}
