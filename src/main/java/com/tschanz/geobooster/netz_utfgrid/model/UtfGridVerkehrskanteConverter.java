package com.tschanz.geobooster.netz_utfgrid.model;

import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz_maptile.model.VerkehrskanteStyle;
import com.tschanz.geobooster.netz_repo.service.VerkehrskanteRepo;
import com.tschanz.geobooster.utfgrid.model.UtfGridLineItem;
import com.tschanz.geobooster.util.model.KeyValue;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;


@RequiredArgsConstructor
public class UtfGridVerkehrskanteConverter {
    private final VerkehrskanteRepo vkRepo;


    public UtfGridLineItem toUtfGrid(VerkehrskanteVersion vkVersion, float zoomLevel) {
        var hst1E = this.vkRepo.getStartHaltestelle(vkVersion);
        var hst2E = this.vkRepo.getEndHaltestelle(vkVersion);
        var hst1V = this.vkRepo.getStartHaltestelleVersion(vkVersion);
        var hst2V = this.vkRepo.getEndHaltestelleVersion(vkVersion);

        return new UtfGridLineItem(
            CoordinateConverter.convertToEpsg3857(hst1V.getCoordinate()),
            CoordinateConverter.convertToEpsg3857(hst2V.getCoordinate()),
            VerkehrskanteStyle.WIDTH.getWidth(zoomLevel),
            Arrays.asList(
                new KeyValue<>("id", "VERKEHRSKANTEN." + vkVersion.getId()),
                new KeyValue<>("ID", vkVersion.getId()),
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
                new KeyValue<>("GUELTIG_VON", vkVersion.getGueltigVon()),
                new KeyValue<>("GUELTIG_BIS", vkVersion.getGueltigBis()),
                new KeyValue<>("TERMINIERT_PER", vkVersion.getTerminiertPer())
            )
        );
    }
}
