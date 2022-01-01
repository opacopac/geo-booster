package com.tschanz.geobooster.netz_utfgrid.model;

import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz.service.TarifkanteRepo;
import com.tschanz.geobooster.netz_graphics.model.TarifkanteStyle;
import com.tschanz.geobooster.utfgrid.model.UtfGridLineItem;
import com.tschanz.geobooster.util.model.KeyValue;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;


@RequiredArgsConstructor
public class UtfGridTarifkanteConverter {
    private final TarifkanteRepo tkRepo;


    public UtfGridLineItem toUtfGrid(TarifkanteVersion tkVersion, float zoomLevel) {
        var hst1E = this.tkRepo.getStartHaltestelle(tkVersion);
        var hst2E = this.tkRepo.getEndHaltestelle(tkVersion);
        var hst1V = this.tkRepo.getStartHaltestelleVersion(tkVersion);
        var hst2V = this.tkRepo.getEndHaltestelleVersion(tkVersion);

        return new UtfGridLineItem(
            CoordinateConverter.convertToEpsg3857(hst1V.getCoordinate()),
            CoordinateConverter.convertToEpsg3857(hst2V.getCoordinate()),
            TarifkanteStyle.WIDTH.getWidth(zoomLevel),
            Arrays.asList(
                new KeyValue<>("id", "TARIFKANTEN." + tkVersion.getId()),
                new KeyValue<>("ID", tkVersion.getId()),
                new KeyValue<>("HS1_ID", hst1E.getId()),
                new KeyValue<>("HS1_UIC_CODE", hst1E.getUicCode()),
                new KeyValue<>("HS1_NAME", hst1V.getName()),
                new KeyValue<>("HS1_LAT", hst1V.getCoordinate().getLatitude()),
                new KeyValue<>("HS1_LNG", hst1V.getCoordinate().getLongitude()),
                new KeyValue<>("HS2_ID", hst2E.getId()),
                new KeyValue<>("HS2_UIC_CODE", hst2E.getUicCode()),
                new KeyValue<>("HS2_NAME", hst2V.getName()),
                new KeyValue<>("HS2_LAT", hst2V.getCoordinate().getLatitude()),
                new KeyValue<>("HS2_LNG", hst2V.getCoordinate().getLongitude()),
                new KeyValue<>("GUELTIG_VON", tkVersion.getGueltigVon()),
                new KeyValue<>("GUELTIG_BIS", tkVersion.getGueltigBis()),
                new KeyValue<>("TERMINIERT_PER", tkVersion.getTerminiertPer())
            )
        );
    }
}
