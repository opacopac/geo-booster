package com.tschanz.geobooster.netz_utfgrid.model;

import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz_graphics.model.TarifkanteStyle;
import com.tschanz.geobooster.utfgrid.model.UtfGridLineItem;
import com.tschanz.geobooster.util.model.KeyValue;

import java.util.Arrays;


public class UtfGridTarifkanteConverter {
    public static UtfGridLineItem toUtfGrid(TarifkanteVersion tarifkanteVersion, float zoomLevel) {
        return new UtfGridLineItem(
            CoordinateConverter.convertToEpsg3857(tarifkanteVersion.getStartCoordinate()),
            CoordinateConverter.convertToEpsg3857(tarifkanteVersion.getEndCoordinate()),
            TarifkanteStyle.WIDTH.getWidth(zoomLevel),
            Arrays.asList(
                new KeyValue<>("id", "TARIFKANTEN." + tarifkanteVersion.getVersionInfo().getId()),
                new KeyValue<>("ID", tarifkanteVersion.getVersionInfo().getId()),
                new KeyValue<>("HS1_ID", tarifkanteVersion.getHaltestelle1().getElementInfo().getId()),
                new KeyValue<>("HS1_UIC_CODE", tarifkanteVersion.getHaltestelle1().getUicCode()),
                new KeyValue<>("HS1_NAME", tarifkanteVersion.getHaltestelle1Version().getName()),
                new KeyValue<>("HS1_LAT", tarifkanteVersion.getStartCoordinate().getLatitude()),
                new KeyValue<>("HS1_LNG", tarifkanteVersion.getStartCoordinate().getLongitude()),
                new KeyValue<>("HS2_ID", tarifkanteVersion.getHaltestelle2().getElementInfo().getId()),
                new KeyValue<>("HS2_UIC_CODE", tarifkanteVersion.getHaltestelle2().getUicCode()),
                new KeyValue<>("HS2_NAME", tarifkanteVersion.getHaltestelle2Version().getName()),
                new KeyValue<>("HS2_LAT", tarifkanteVersion.getEndCoordinate().getLatitude()),
                new KeyValue<>("HS2_LNG", tarifkanteVersion.getEndCoordinate().getLongitude()),
                new KeyValue<>("GUELTIG_VON", tarifkanteVersion.getVersionInfo().getGueltigVon()),
                new KeyValue<>("GUELTIG_BIS", tarifkanteVersion.getVersionInfo().getGueltigBis()),
                new KeyValue<>("TERMINIERT_PER", null) // TODO
            )
        );
    }
}
